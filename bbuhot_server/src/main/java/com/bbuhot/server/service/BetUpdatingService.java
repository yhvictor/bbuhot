package com.bbuhot.server.service;

import com.bbuhot.server.domain.Authority;
import com.bbuhot.server.domain.BettingOnGame;
import com.bbuhot.server.domain.BettingOnGame.BettingOnGameException;
import com.bbuhot.server.entity.BetEntity;
import java.util.List;
import javax.inject.Inject;


class BetUpdatingService extends AbstractProtoBufService<BetRequest, BetReply> {

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

  static Game.Bet.Builder toBet(BetEntity betEntity) {
    Game.Bet.Builder betBuild = Game.Bet.newBuilder();
    betBuild.setBettingOptionId(betEntity.getBettingOptionId());
    betBuild.setMoney(betEntity.getBetAmount());
    return betBuild;
  }

  @Override
  BetReply callProtobufServiceImpl(BetRequest betRequest) {
    AuthReply authReply = authority.auth(betRequest.getAuth(), false);
    if (authReply.getErrorCode != AuthErrorCode.NO_ERROR) {
      // Failed to auth.
      return BetReply.newBuilder().setAuthErrorCode(authReply.getErrorCode()).build();
    }

    BetReply.Builder reply = BetReply.newBuilder().setAuthErrorCode(authReply.getErrorCode());

    if (betRequest.hasBets()) {
      try {
        List<BetEntity> bets = bettingOnGame.bettingOnGame(
            betRequest.getGameId(),
            betRequest.getAuth().getUid(),
            betRequest.getBets());
        for (int i = 0; i < bets.size(); i++) {
          reply.addBets(BetUpdatingService.toBet(bets.get(i)));
        }
      } catch (BettingOnGameException e) {
        reply.setBetErrorCode(e.getBetErrorCode());
        List<BetEntity> bets = bettingOnGame.getOriginalBets(
            betRequest.getGameId(),
            betRequest.getAuth().getUid());
        for (int i = 0; i < bets.size(); i++) {
          reply.addBets(BetUpdatingService.toBet(bets.get(i)));
        }
      }
    } else {
      bettingOnGame.withdrawFromGame(betRequest.getGameId(),
          betRequest.getAuth().getUid());
      return reply.build();
    }

    return reply.build();
  }
}
