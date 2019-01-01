package com.bbuhot.server.domain;

import com.bbuhot.server.entity.BetEntity;
import com.bbuhot.server.entity.ExtcreditsEntity;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.BetQueries;
import com.bbuhot.server.persistence.ExtcreditsQueries;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.service.BetReply.BetErrorCode;
import com.bbuhot.server.service.Game.Bet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class BettingOnGame {

  private final GameQueries gameQueries;
  private final BetQueries betQueries;
  private final ExtcreditsQueries extcreditsQueries;

  @Inject
  public BettingOnGame(GameQueries gameQueries, BetQueries betQueries, ExtcreditsQueries extcreditsQueries) {
    this.gameQueries = gameQueries;
    this.betQueries = betQueries;
    this.extcreditsQueries = extcreditsQueries;
  }

  private GameEntity getGameEntity(int gameId) {
    Date date = new Date();

    GameEntity gameEntity = gameQueries.queryById(gameId).orElseGet(() -> {
      throw new IllegalStateException("No game with such id: " + gameId);
    });

    if (GameEntityStatus.valueOf(gameEntity.getStatus()) != GameEntityStatus.PUBLISHED) {
      throw new IllegalStateException("Not Betable for game: " + gameId);
    }

    if (date.getTime() > gameEntity.getEndTimeMs().getTime() ) {
      throw new IllegalStateException("Betting time over for game: " + gameId);
    }

    return gameEntity;
  }

  public List<BetEntity> bettingOnGame(int gameId, int uid, List<Bet> bets) throws BettingOnGameException {
    GameEntity gameEntity = getGameEntity(gameId);

    if (bets.size() > gameEntity.getBetOptionLimit()) {
      throw new BettingOnGameException(BetErrorCode.OPTION_TOO_MANY);
    }

    List<BetEntity> betEntities = new ArrayList<>();
    int total = 0;
    for (Bet bet:bets) {
      if (bet.getBettingOptionId() >= gameEntity.getBettingOptionEntities().size()) {
        throw new IllegalStateException("Betting option Id out of range:" + bet.getBettingOptionId());
      }

      if (bet.getMoney() < gameEntity.getBetAmountLowest()) {
        throw new BettingOnGameException(BetErrorCode.MONEY_TOO_LOW);
      } else if(bet.getMoney() > gameEntity.getBetAmountHighest()) {
        throw new BettingOnGameException(BetErrorCode.MONEY_TOO_HIGH);
      }

      total += bet.getMoney();

      BetEntity betEntity = new BetEntity();
      betEntity.setUid(uid);
      betEntity.setGameId(gameId);
      betEntity.setBettingOptionId(bet.getBettingOptionId());
      betEntity.setBetAmount(bet.getMoney());

      betEntities.add(betEntity);
    }

    int betted = betQueries.queryBetted(gameId, uid);
    ExtcreditsEntity extcreditsEntity = extcreditsQueries.queryById(uid).orElseGet(() -> {
      throw new IllegalStateException("No extcredits for user: " + uid);
    });

    int remaining = extcreditsEntity.getExtcredits2();

    if (total > remaining + betted) {
      throw new BettingOnGameException(BetErrorCode.NO_ENOUGH_MONEY);
    }

    betQueries.saveBets(betEntities, gameId, uid);

    extcreditsEntity.setExtcredits2(remaining + betted - total);
    extcreditsQueries.save(extcreditsEntity);

    return betEntities;
  }

  public void withdrawFromGame(int gameId, int uid) {
    GameEntity gameEntity = getGameEntity(gameId);
    int betted = betQueries.queryBetted(gameId, uid);

    ExtcreditsEntity extcreditsEntity = extcreditsQueries.queryById(uid).orElseGet(() -> {
      throw new IllegalStateException("No extcredits for user: " + uid);
    });
    int remaining = extcreditsEntity.getExtcredits2();

    betQueries.deleteBets(gameId, uid);
    extcreditsEntity.setExtcredits2(remaining + betted);
    extcreditsQueries.save(extcreditsEntity);
  }

  public List<BetEntity> getOriginalBets(int gameId, int uid) {
    return betQueries.queryByGameAndUser(gameId, uid);
  }

  public static final class BettingOnGameException extends Exception {
    private BetErrorCode betErrorCode;

    private BettingOnGameException(BetErrorCode betErrorCode) {
      this.betErrorCode = betErrorCode;
    }

    public BetErrorCode getBetErrorCode() {
      return betErrorCode;
    }
  }
}
