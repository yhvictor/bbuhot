package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameStatus;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.Game.Status;
import java.util.List;
import javax.inject.Inject;

class ListGameService extends AbstractProtobufService<ListGameRequest, ListGameReply> {

  private final Authority authority;
  private final GameQueries gameQueries;

  @Inject
  ListGameService(Authority authority, GameQueries gameQueries) {
    this.authority = authority;
    this.gameQueries = gameQueries;
  }

  @Override
  ListGameRequest getInputMessageDefaultInstance() {
    return ListGameRequest.getDefaultInstance();
  }

  @Override
  ListGameReply callProtobufServiceImpl(ListGameRequest listGameRequest) {
    if (listGameRequest.getGameStatus() == Status.DRAFT && !listGameRequest.getIsAdminRequest()) {
      throw new IllegalStateException("Normal user are not allowed to query draft game.");
    }

    ListGameReply.Builder reply = ListGameReply.newBuilder();

    AuthReply authReply =
        authority.auth(listGameRequest.getAuth(), listGameRequest.getIsAdminRequest());
    reply.setAuthErrorCode(authReply.getErrorCode());
    if (reply.getAuthErrorCode() != AuthErrorCode.NO_ERROR) {
      return reply.build();
    }

    List<GameEntity> gameEntities =
        gameQueries.queryByStatus(GameStatus.valueOf(listGameRequest.getGameStatus()));

    for (GameEntity gameEntity : gameEntities) {
      if ((!listGameRequest.getIsAdminRequest()) && !gameEntity.isNormalUserVisible()) {
        // TODO(yhvictor): move this to db query.
        continue;
      }

      reply.addGames(GameUpdatingService.toGame(gameEntity));
    }

    if (!listGameRequest.getIsAdminRequest()) {
      // TODO(yhvictor): append bets.
    }

    return reply.build();
  }
}
