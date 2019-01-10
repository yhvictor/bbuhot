package com.bbuhot.server.domain;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.BetQueries;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.service.AdminGameStatusReply.ErrorCode;
import com.bbuhot.server.util.BbuhotThreadPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

public class GameStatusChanging {

  private static final ImmutableMap<GameEntityStatus, Set<GameEntityStatus>> ALLOWED_STATUS_CHANGE =
      new ImmutableMap.Builder<GameEntityStatus, Set<GameEntityStatus>>()
          .put(GameEntityStatus.DRAFT, ImmutableSet.of(GameEntityStatus.PUBLISHED))
          .put(
              GameEntityStatus.PUBLISHED,
              ImmutableSet.of(
                  GameEntityStatus.PUBLISHED, GameEntityStatus.SETTLED, GameEntityStatus.CANCELLED))
          .put(
              GameEntityStatus.SETTLED,
              ImmutableSet.of(GameEntityStatus.PUBLISHED, GameEntityStatus.SETTLED))
          .put(
              GameEntityStatus.CANCELLED,
              ImmutableSet.of(GameEntityStatus.PUBLISHED, GameEntityStatus.CANCELLED))
          .build();

  private static final ConcurrentHashMap<Integer, AtomicBoolean> LOCK_MAP =
      new ConcurrentHashMap<>();

  private final GameQueries gameQueries;

  private final BetQueries betQueries;

  @Inject
  public GameStatusChanging(GameQueries gameQueries, BetQueries betQueries) {
    this.gameQueries = gameQueries;
    this.betQueries = betQueries;
  }

  public GameEntity changeGameStatus(int gameId, GameEntityStatus newStatus, int winningOption)
      throws GameStatusChangingException {
    GameEntity gameEntity =
        gameQueries
            .queryById(gameId)
            .orElseGet(
                () -> {
                  throw new IllegalStateException("No game with such id: " + gameId);
                });

    GameEntityStatus oldStatus = GameEntityStatus.valueOf(gameEntity.getStatus());

    if (!ALLOWED_STATUS_CHANGE.get(oldStatus).contains(newStatus)) {
      throw new IllegalStateException(
          "Status change not allowed: " + oldStatus + " -> " + newStatus);
    }
    if (newStatus == GameEntityStatus.SETTLED) {
      if (winningOption >= gameEntity.getBettingOptionEntities().size()) {
        throw new IllegalStateException(
            "Winning option too large: "
                + winningOption
                + " / "
                + gameEntity.getBettingOptionEntities().size());
      }
      if (winningOption < -1) {
        throw new IllegalStateException("Winning option too small: " + winningOption);
      }
    } else {
      if (winningOption != -2) {
        throw new IllegalStateException("Internal error.");
      }
    }

    AtomicBoolean gameLock = LOCK_MAP.computeIfAbsent(gameId, unused -> new AtomicBoolean());
    if (!gameLock.compareAndSet(false, true)) {
      throw new GameStatusChangingException(ErrorCode.LOCKED);
    }

    AtomicBoolean releasingLock = new AtomicBoolean();
    Runnable releaseLock =
        () -> {
          if (releasingLock.compareAndSet(false, true)) {
            gameLock.set(false);
          }
        };

    try {
      gameEntity.setStatus(newStatus.value);
      gameEntity.setWinningBetOption(winningOption);
      gameQueries.save(gameEntity);
    } catch (Throwable t) {
      releaseLock.run();
      throw t;
    }

    if (oldStatus != GameEntityStatus.DRAFT) {
      updateBets(gameEntity, releaseLock);
    } else {
      releaseLock.run();
    }

    return gameEntity;
  }

  private void updateBets(GameEntity gameEntity, Runnable releaseLock) {
    BbuhotThreadPool.scheduleExecutor
        .schedule(() -> updateBetsImpl(gameEntity), 3000, TimeUnit.MILLISECONDS)
        .addListener(releaseLock, MoreExecutors.directExecutor());
  }

  private void updateBetsImpl(GameEntity gameEntity) {
    GameEntityStatus status = GameEntityStatus.valueOf(gameEntity.getStatus());
    int gameId = gameEntity.getId();

    switch (status) {
      case PUBLISHED:
        betQueries.revokeCancellationOrAllRewards(gameId);
        break;
      case SETTLED:
        int winningOptionId = gameEntity.getWinningBetOption();
        int odds = gameEntity.getBettingOptionEntities().get(winningOptionId).getOdds();
        betQueries.rewardAllBets(gameId, winningOptionId, odds);
        break;
      case CANCELLED:
        betQueries.revokeAllBets(gameId);
        break;
      default:
        throw new IllegalStateException("Internal error. wrong game status");
    }
  }

  public static final class GameStatusChangingException extends Exception {

    private ErrorCode errorCode;

    private GameStatusChangingException(ErrorCode errorCode) {
      this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
      return errorCode;
    }
  }
}
