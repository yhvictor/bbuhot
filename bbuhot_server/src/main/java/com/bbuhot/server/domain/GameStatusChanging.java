package com.bbuhot.server.domain;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.service.AdminGameStatusReply.ErrorCode;
import com.bbuhot.server.util.BbuhotThreadPool;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

public class GameStatusChanging {

  private static final Map<GameEntityStatus, Set<GameEntityStatus>> ALLOWED_STATUS_CHANGE = new ImmutableMap.Builder<GameEntityStatus, Set<GameEntityStatus>>()
      .put(GameEntityStatus.DRAFT, ImmutableSet.of(GameEntityStatus.PUBLISHED))
      .put(GameEntityStatus.PUBLISHED,
          ImmutableSet.of(GameEntityStatus.PUBLISHED, GameEntityStatus.SETTLED))
      .put(GameEntityStatus.SETTLED,
          ImmutableSet.of(GameEntityStatus.PUBLISHED, GameEntityStatus.SETTLED)).build();

  private static final ConcurrentHashMap<Integer, AtomicBoolean> LOCK_MAP = new ConcurrentHashMap<>();

  private final GameQueries gameQueries;

  @Inject
  public GameStatusChanging(GameQueries gameQueries) {
    this.gameQueries = gameQueries;
  }

  public GameEntity changeGameStatus(int gameId, GameEntityStatus newStatus, int winningOption)
      throws GameStatusChangingException {
    GameEntity gameEntity = gameQueries.queryById(gameId).orElseGet(() -> {
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
            "Winning option too large: " + winningOption + " / " + gameEntity
                .getBettingOptionEntities().size());
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
    Runnable releaseLock = () -> {
      if (releasingLock.compareAndSet(false, true)) {
        gameLock.set(false);
      }
    };

    try {
      gameEntity.setStatus(newStatus.value);
      gameEntity.setWinningBetOption(winningOption);
      gameQueries.update(gameEntity);
    } catch (Throwable t) {
      releaseLock.run();
      throw t;
    }

    if (oldStatus != GameEntityStatus.DRAFT) {
      updateBets(gameEntity, releaseLock);
    }

    return gameEntity;
  }

  private void updateBets(GameEntity gameEntity, Runnable releaseLock) {
    // TODO(yhvictor): update this.
    BbuhotThreadPool.scheduleExecutor.schedule(releaseLock, 3000, TimeUnit.MILLISECONDS);
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