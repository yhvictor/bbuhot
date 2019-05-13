package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.domain.GameStatusChanging;
import com.bbuhot.server.domain.GameStatusChanging.GameStatusChangingException;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;

import javax.inject.Inject;

class AdminGameStatusChangeService
    extends AbstractProtobufService<AdminGameStatusRequest, AdminGameStatusReply> {

  private final Authority authority;
  private final GameStatusChanging gameStatusChanging;

  @Inject
  AdminGameStatusChangeService(Authority authority, GameStatusChanging gameStatusChanging) {
    this.authority = authority;
    this.gameStatusChanging = gameStatusChanging;
  }

  @Override
  AdminGameStatusRequest getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes) {
    AdminGameStatusRequest.Builder builder = AdminGameStatusRequest.newBuilder();
    exchange.mergeFieldsFromBody(builder, bytes);
    AuthRequest authRequest = exchange.generateAuthRequestFromCookie();
    if (authRequest != null) {
      builder.setAuth(authRequest);
    }
    return builder.build();
  }

  @Override
  AdminGameStatusReply callProtobufServiceImpl(AdminGameStatusRequest gameStatusRequest) {
    AdminGameStatusReply.Builder reply = AdminGameStatusReply.newBuilder();
    AuthReply authReply = authority.auth(gameStatusRequest.getAuth(), true);
    reply.setAuthErrorCode(authReply.getErrorCode());
    if (reply.getAuthErrorCode() != AuthErrorCode.NO_ERROR) {
      return reply.build();
    }

    if (gameStatusRequest.getGameStatus() == Game.Status.SETTLED
        && !gameStatusRequest.hasWinningOption()) {
      throw new IllegalStateException("Game.Status is SETTLED, but winning_option is not set.");
    }

    int winningOption =
        gameStatusRequest.getGameStatus() == Game.Status.SETTLED
            ? gameStatusRequest.getWinningOption()
            : -2;

    try {
      GameEntity gameEntity =
          gameStatusChanging.changeGameStatus(
              gameStatusRequest.getGameId(),
              GameEntityStatus.valueOf(gameStatusRequest.getGameStatus()),
              winningOption);
      reply.setGame(AdminGameUpdatingService.toGame(gameEntity));
    } catch (GameStatusChangingException e) {
      reply.setGameStatusErrorCode(e.getErrorCode());
    }

    return reply.build();
  }
}
