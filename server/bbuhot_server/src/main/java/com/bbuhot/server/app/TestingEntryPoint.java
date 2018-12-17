package com.bbuhot.server.app;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BettingOptionEntity;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameStatus;
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
    BettingOptionEntity bettingOptionEntity = new BettingOptionEntity();
    bettingOptionEntity.setName("123123");
    bettingOptionEntity.setOdds(123123);

    BettingOptionEntity bettingOptionEntity2 = new BettingOptionEntity();
    bettingOptionEntity2.setName("2123123");
    bettingOptionEntity2.setOdds(2123123);
    gameEntity.getBetEntities().add(bettingOptionEntity);
    gameEntity.getBetEntities().add(bettingOptionEntity2);

    gameQueries.create(gameEntity);
    System.out.println("adfasdfasdfsdaf" + gameEntity.getId());
    gameEntity.setName("yyyhhhh");

    gameEntity.getBetEntities().remove(1);
    BettingOptionEntity bettingOptionEntity3 = new BettingOptionEntity();
    bettingOptionEntity3.setName("gsdfgaef");
    bettingOptionEntity3.setOdds(231251234);
    gameEntity.getBetEntities().add(bettingOptionEntity3);

    gameQueries.update(gameEntity);

    List<GameEntity> games = gameQueries.queryByStatus(GameStatus.DRAFT);
    for (GameEntity game : games) {
      System.out.println(game.getId());
      System.out.println(game.getName());
      System.out.println(game.getDescription());
      for (BettingOptionEntity bet : game.getBetEntities()) {
        System.out.println("Bets: " + bet.getName() + " " + bet.getOdds());
      }
    }
  }
}
