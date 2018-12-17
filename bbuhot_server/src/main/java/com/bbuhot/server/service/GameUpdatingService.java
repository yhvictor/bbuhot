package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BettingOptionEntity;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameStatus;
import com.bbuhot.server.service.AdminGameReply.GameErrorCode;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.Game.BettingOption;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

class GameUpdatingService extends AbstractProtobufService<AdminGameRequest, AdminGameReply> {

  private final Authority authority;
  private final GameQueries gameQueries;

  @Inject
  GameUpdatingService(Authority authority, GameQueries gameQueries) {
    this.authority = authority;
    this.gameQueries = gameQueries;
  }

  private static Game toGame(GameEntity gameEntity) {
    Game.Builder gameBuild = Game.newBuilder();

    gameBuild.setId(gameEntity.getId());
    gameBuild.setName(gameEntity.getName());
    gameBuild.setStatus(GameStatus.valueOf(gameEntity.getStatus()).serviceStatus);
    gameBuild.setDescription(gameEntity.getDescription());
    gameBuild.setNormalUserVisible(gameEntity.isNormalUserVisible());
    gameBuild.setBetOptionLimit(gameEntity.getBetOptionLimit());
    gameBuild.setBetAmountLowest(gameEntity.getBetAmountLowest());
    gameBuild.setBetAmountHighest(gameEntity.getBetAmountHighest());
    gameBuild.setEndTimeMs(gameEntity.getEndTimeMs().getTime());

    for (BettingOptionEntity betOption : gameEntity.getBetEntities()) {
      gameBuild.addBettingOptions(
          BettingOption.newBuilder().setName(betOption.getName()).setOdds(betOption.getOdds()));
    }

    return gameBuild.build();
  }

  @Override
  AdminGameRequest getInputMessageDefaultInstance() {
    return AdminGameRequest.getDefaultInstance();
  }

  @Override
  AdminGameReply callProtobufServiceImpl(AdminGameRequest adminGameRequest) {
    AuthErrorCode errorCode = authority.auth(adminGameRequest.getAuth()).getErrorCode();
    if (errorCode != AuthErrorCode.NO_ERROR) {
      // Failed to auth.
      return AdminGameReply.newBuilder().setAuthErrorCode(errorCode).build();
    }

    AdminGameReply.Builder reply = AdminGameReply.newBuilder().setAuthErrorCode(errorCode);

    int id = adminGameRequest.getGame().getId();

    GameEntity gameEntity;
    if (id != -1) { // update
      Optional<GameEntity> optionalGame = gameQueries.queryById(id);
      if (optionalGame.isEmpty()) {
        return reply.setGameErrorCode(GameErrorCode.NO_SUCH_GAME).build();
      }
      gameEntity = optionalGame.get();
    } else { // create
      gameEntity = new GameEntity();
    }

    reply.setGameErrorCode(GameErrorCode.NO_ERROR);
    mergeToEntity(gameEntity, adminGameRequest.getGame());

    if (id != -1) {
      gameQueries.update(gameEntity);
    } else {
      gameQueries.create(gameEntity);
    }

    Game game = toGame(gameEntity);
    return reply.setGame(game).build();
  }

  private void mergeToEntity(GameEntity gameEntity, Game game) {
    gameEntity.setName(game.getName());
    gameEntity.setDescription(game.getDescription());
    gameEntity.setBetOptionLimit(game.getBetOptionLimit());
    gameEntity.setBetAmountLowest(game.getBetAmountLowest());
    gameEntity.setBetAmountHighest(game.getBetAmountHighest());
    gameEntity.setEndTimeMs(new Timestamp(game.getEndTimeMs()));
    gameEntity.setNormalUserVisible(game.getNormalUserVisible());

    List<BettingOptionEntity> betOptionEntities = gameEntity.getBetEntities();

    while (betOptionEntities.size() > game.getBettingOptionsCount()) {
      betOptionEntities.remove(betOptionEntities.size() - 1);
    }
    while (betOptionEntities.size() < game.getBettingOptionsCount()) {
      betOptionEntities.add(new BettingOptionEntity());
    }

    for (int i = 0; i < game.getBettingOptionsCount(); i++) {
      BettingOptionEntity bettingOptionEntity = betOptionEntities.get(i);
      BettingOption bettingOption = game.getBettingOptions(i);

      bettingOptionEntity.setName(bettingOption.getName());
      bettingOptionEntity.setOdds(bettingOption.getOdds());
    }
  }
}
