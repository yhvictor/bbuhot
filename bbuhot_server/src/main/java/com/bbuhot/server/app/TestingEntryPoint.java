package com.bbuhot.server.app;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BetEntity;
import com.bbuhot.server.persistence.GameQueries;
import java.util.List;
import javax.inject.Inject;

/**
 * A class for fast testing. Will be remove later.
 */
public class TestingEntryPoint {

  private final GameQueries gameQueries;

  @Inject
  TestingEntryPoint(GameQueries gameQueries) {
    this.gameQueries = gameQueries;
  }

  public static void main(final String[] args) {
    EntryPoint.appComponent = DaggerAppComponent.create();
    EntryPoint.appComponent.testingEntryPoint().run();
  }

  private void run() {
    GameEntity gameEntity = new GameEntity();
    gameEntity.setDescription("asdfasdfasdf");
    gameEntity.setName("123123asdfasdfasdf");
    BetEntity betEntity = new BetEntity();
    betEntity.setName("123123");
    betEntity.setOdds(123123);

    BetEntity betEntity2 = new BetEntity();
    betEntity2.setName("2123123");
    betEntity2.setOdds(2123123);
    gameEntity.getBetEntities().add(betEntity);
    gameEntity.getBetEntities().add(betEntity2);

    gameQueries.create(gameEntity);
    System.out.println("adfasdfasdfsdaf" + gameEntity.getId());
    gameEntity.setName("yyyhhhh");

    gameEntity.getBetEntities().remove(1);
    BetEntity betEntity3 = new BetEntity();
    betEntity3.setName("gsdfgaef");
    betEntity3.setOdds(231251234);
    gameEntity.getBetEntities().add(betEntity3);

    gameQueries.update(gameEntity);

    List<GameEntity> games = gameQueries.queryByStatus(0);
    for (GameEntity game : games) {
      System.out.println(game.getId());
      System.out.println(game.getName());
      System.out.println(game.getDescription());
      for (BetEntity bet : game.getBetEntities()) {
        System.out.println("Bets: " + bet.getName() + " " + bet.getOdds());
      }
    }
  }
}
