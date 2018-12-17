package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BettingOptionEntity;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameStatus;
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

  @Override
  AdminGameRequest getInputMessageDefaultInstance() {
    return AdminGameRequest.getDefaultInstance();
  }

  // TODO(yh_victor): move to util?
  static Game.Builder toGame(GameEntity gameEntity) {
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

    return gameBuild;
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

  @Override
  AdminGameReply callProtobufServiceImpl(AdminGameRequest adminGameRequest) {
    AuthReply authReply = authority.auth(adminGameRequest.getAuth(), /* checkIsAdmin= */ true);
    if (authReply.getErrorCode() != AuthErrorCode.NO_ERROR) {
      // Failed to auth.
      return AdminGameReply.newBuilder().setAuthErrorCode(authReply.getErrorCode()).build();
    }

    AdminGameReply.Builder reply =
        AdminGameReply.newBuilder().setAuthErrorCode(authReply.getErrorCode());

    int id = adminGameRequest.getGame().getId();

    GameEntity gameEntity;
    if (id != -1) { // update
      Optional<GameEntity> optionalGame = gameQueries.queryById(id);
      if (optionalGame.isEmpty()) {
        throw new IllegalStateException("No such game.");
      }
      gameEntity = optionalGame.get();
    } else { // create
      gameEntity = new GameEntity();
    }

    mergeToEntity(gameEntity, adminGameRequest.getGame());

    if (id != -1) {
      gameQueries.update(gameEntity);
    } else {
      gameQueries.create(gameEntity);
    }

    Game.Builder game = toGame(gameEntity);
    return reply.setGame(game).build();
  }
}
