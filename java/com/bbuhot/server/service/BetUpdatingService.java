package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.domain.BettingOnGame;
import com.bbuhot.server.domain.BettingOnGame.BettingOnGameException;
import com.bbuhot.server.entity.BetEntity;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.BetReply.BetErrorCode;

import javax.inject.Inject;
import java.util.List;

class BetUpdatingService extends AbstractProtobufService<BetRequest, BetReply> {

  private final Authority authority;
  private final BettingOnGame bettingOnGame;

  @Inject
  BetUpdatingService(Authority authority, BettingOnGame bettingOnGame) {
    this.authority = authority;
    this.bettingOnGame = bettingOnGame;
  }

  static Game.Bet toBet(BetEntity betEntity) {
    Game.Bet.Builder betBuild =
        Game.Bet.newBuilder()
            .setBettingOptionId(betEntity.getBettingOptionId())
            .setMoney(betEntity.getBetAmount());
    return betBuild.build();
  }

  @Override
  BetRequest getInputMessage(HttpServerExchangeMessageWrapper exchange, byte[] bytes) {
    BetRequest.Builder builder = BetRequest.newBuilder();
    exchange.mergeFieldsFromBody(builder, bytes);
    AuthRequest authRequest = exchange.generateAuthRequestFromCookie();
    if (authRequest != null) {
      builder.setAuth(authRequest);
    }
    return builder.build();
  }

  @Override
  BetReply callProtobufServiceImpl(BetRequest betRequest) {
    AuthReply authReply = authority.auth(betRequest.getAuth(), /* checkIsAdmin= */ false);
    if (authReply.getErrorCode() != AuthErrorCode.NO_ERROR) {
      // Failed to auth.
      return BetReply.newBuilder().setAuthErrorCode(authReply.getErrorCode()).build();
    }

    BetReply.Builder reply = BetReply.newBuilder().setAuthErrorCode(authReply.getErrorCode());

    if (betRequest.getBetsCount() == 0) {
      bettingOnGame.withdrawFromGame(betRequest.getGameId(), authReply.getUser().getUid());
      reply.setBetErrorCode(BetErrorCode.NO_ERROR);
      return reply.build();
    }

    try {
      List<BetEntity> betEntities =
          bettingOnGame.bettingOnGame(
              betRequest.getGameId(), authReply.getUser().getUid(), betRequest.getBetsList());
      reply.setBetErrorCode(BetErrorCode.NO_ERROR);
      for (BetEntity betEntity : betEntities) {
        reply.addBets(toBet(betEntity));
      }
    } catch (BettingOnGameException e) {
      reply.setBetErrorCode(e.getBetErrorCode());
      List<BetEntity> betEntities =
          bettingOnGame.getOriginalBets(betRequest.getGameId(), authReply.getUser().getUid());
      for (BetEntity betEntity : betEntities) {
        reply.addBets(toBet(betEntity));
      }
    }

    return reply.build();
  }
}
