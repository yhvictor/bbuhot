package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.domain.BettingOnGame;
import com.bbuhot.server.domain.BettingOnGame.BettingOnGameException;
import com.bbuhot.server.entity.BetEntity;
import com.bbuhot.server.service.AuthReply.AuthErrorCode;
import com.bbuhot.server.service.BetReply.BetErrorCode;
import java.util.List;
import javax.inject.Inject;

class BetUpdatingService extends AbstractProtobufService<BetRequest, BetReply> {

  private final Authority authority;
  private final BettingOnGame bettingOnGame;

  @Inject
  BetUpdatingService(Authority authority, BettingOnGame bettingOnGame) {
    this.authority = authority;
    this.bettingOnGame = bettingOnGame;
  }

  @Override
  BetRequest getInputMessageDefaultInstance() {
    return BetRequest.getDefaultInstance();
  }

  static Game.Bet toBet(BetEntity betEntity) {
    Game.Bet.Builder betBuild =
        Game.Bet.newBuilder()
            .setBettingOptionId(betEntity.getBettingOptionId())
            .setMoney(betEntity.getBetAmount());
    return betBuild.build();
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
      bettingOnGame.withdrawFromGame(betRequest.getGameId(), betRequest.getAuth().getUid());
      reply.setBetErrorCode(BetErrorCode.NO_ERROR);
      return reply.build();
    }

    try {
      List<BetEntity> betEntities =
          bettingOnGame.bettingOnGame(
              betRequest.getGameId(), betRequest.getAuth().getUid(), betRequest.getBetsList());
      reply.setBetErrorCode(BetErrorCode.NO_ERROR);
      for (BetEntity betEntity : betEntities) {
        reply.addBets(toBet(betEntity));
      }
    } catch (BettingOnGameException e) {
      reply.setBetErrorCode(e.getBetErrorCode());
      List<BetEntity> betEntities =
          bettingOnGame.getOriginalBets(betRequest.getGameId(), betRequest.getAuth().getUid());
      for (BetEntity betEntity : betEntities) {
        reply.addBets(toBet(betEntity));
      }
    }

    return reply.build();
  }
}
