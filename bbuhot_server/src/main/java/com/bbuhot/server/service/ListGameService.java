package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.entity.BetEntity;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.BetQueries;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.Game.Status;
import java.util.List;
import javax.inject.Inject;

class ListGameService extends AbstractProtobufService<ListGameRequest, ListGameReply> {

  private final Authority authority;
  private final GameQueries gameQueries;
  private final BetQueries betQueries;

  @Inject
  ListGameService(Authority authority, GameQueries gameQueries, BetQueries betQueries) {
    this.authority = authority;
    this.gameQueries = gameQueries;
    this.betQueries = betQueries;
  }

  @Override
  ListGameRequest getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes) {
    ListGameRequest.Builder builder = ListGameRequest.newBuilder();
    exchange.mergeFieldsFromBody(builder, bytes);
    AuthRequest authRequest = exchange.generateAuthRequestFromCookie();
    if (authRequest != null) {
      builder.setAuth(authRequest);
    }
    return builder.build();
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
        gameQueries.queryByStatus(GameEntityStatus.valueOf(listGameRequest.getGameStatus()));

    for (GameEntity gameEntity : gameEntities) {
      if (!listGameRequest.getIsAdminRequest() && !gameEntity.isNormalUserVisible()) {
        // TODO(yhvictor): move this to db query.
        continue;
      }

      Game.Builder gameBuilder = AdminGameUpdatingService.toGame(gameEntity);

      if (!listGameRequest.getIsAdminRequest()) {
          List<BetEntity> betEntities =
              betQueries.queryByGameAndUser(gameEntity.getId(), authReply.getUser().getUid());

          for(BetEntity betEntity : betEntities) {
              gameBuilder.addBets(BetUpdatingService.toBet(betEntity));
          }
      }

      reply.addGames(gameBuilder);
    }

    return reply.build();
  }
}
