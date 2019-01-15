package com.bbuhot.server.service;

import static junit.framework.TestCase.assertEquals;

import com.bbuhot.server.entity.BetEntity;
import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.entity.GameEntity.BettingOptionEntity;
import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.BetQueries;
import com.bbuhot.server.persistence.BetQueries.BetEntityStatus;
import com.bbuhot.server.persistence.GameQueries;
import com.bbuhot.server.persistence.GameQueries.GameEntityStatus;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.util.TestMessageUtil;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameListingServiceTest {

  @Inject ListGameService listGameService;

  @Inject BetQueries betQueries;
  @Inject GameQueries gameQueries;
  @Inject UserQueries userQueries;
  @Inject EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
    Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
    TestServiceComponent.getInstance().inject(this);

    // create admin user
    UserEntity user = new UserEntity();
    user.setUid(1);
    user.setUsername("admin");
    user.setGroupId(1);
    user.setPassword("b2b29e4862ffe7a5d1388d1722a9aae5");
    userQueries.save(user);

    // create games
    // draft game
    GameEntity game = new GameEntity();
    game.setId(10);
    game.setName("Ti8 Grand final LGD vs OG");
    game.setDescription("Ti8 Grand final LGD vs OG");
    game.setNormalUserVisible(false);
    game.setStatus(GameEntityStatus.DRAFT.value);
    game.setBetOptionLimit(1);
    game.setBetAmountLowest(100);
    game.setBetAmountHighest(400);
    game.setEndTimeMs(new Timestamp(1535126400000L));
    game.setWinningBetOption(-2);
    BettingOptionEntity option1 = new BettingOptionEntity();
    option1.setName("LGD win");
    option1.setOdds(2000000);
    game.getBettingOptionEntities().add(option1);
    BettingOptionEntity option2 = new BettingOptionEntity();
    option2.setName("OG win");
    option2.setOdds(8000000);
    game.getBettingOptionEntities().add(option2);
    gameQueries.save(game);
    // published game
    GameEntity game2 = new GameEntity();
    game2.setId(11);
    game2.setName("Ti8 Grand final LGD vs OG BO5");
    game2.setDescription("Ti8 Grand final LGD vs OG BO5");
    game2.setNormalUserVisible(true);
    game2.setStatus(GameEntityStatus.PUBLISHED.value);
    game2.setBetOptionLimit(1);
    game2.setBetAmountLowest(100);
    game2.setBetAmountHighest(400);
    game2.setEndTimeMs(new Timestamp(1535126400000L));
    game2.setWinningBetOption(-2);
    BettingOptionEntity option3 = new BettingOptionEntity();
    option3.setName("LGD win");
    option3.setOdds(2000000);
    game2.getBettingOptionEntities().add(option3);
    BettingOptionEntity option4 = new BettingOptionEntity();
    option4.setName("OG win");
    option4.setOdds(8000000);
    game2.getBettingOptionEntities().add(option4);
    gameQueries.save(game2);

    // create bets
    BetEntity bet = new BetEntity();
    bet.setId(1);
    bet.setUid(user.getUid());
    bet.setGameId(game2.getId());
    bet.setBettingOptionId(0);
    bet.setBetAmount(100);
    bet.setStatus(BetEntityStatus.UNSETTLED.value);
    bet.setCreatedAt(new Timestamp(1535126300000L));
    betQueries.save(bet);
  }

  @After
  public void tearDown() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
    entityManager.getTransaction().commit();
  }

  @Test
  public void testGameListing() {
    // listing draft games as admin
    ListGameRequest listGameRequest =
      TestMessageUtil.getResourcesAsMessage(
          ListGameRequest.getDefaultInstance(),
          "com/bbuhot/server/service/admin_list_draft_game_request.json");

    ListGameReply listGameReplyExpected =
      TestMessageUtil.getResourcesAsMessage(
          ListGameReply.getDefaultInstance(),
          "com/bbuhot/server/service/admin_list_draft_game_reply.json");

    ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
    assertEquals(listGameReplyExpected, listGameReply);

    // listing published games as admin
    listGameRequest =
      TestMessageUtil.getResourcesAsMessage(
          ListGameRequest.getDefaultInstance(),
          "com/bbuhot/server/service/admin_list_game_request.json");

    listGameReplyExpected =
      TestMessageUtil.getResourcesAsMessage(
          ListGameReply.getDefaultInstance(),
          "com/bbuhot/server/service/admin_list_game_reply.json");

    listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
    assertEquals(listGameReplyExpected, listGameReply);

    // listing published game as normal user
    listGameRequest =
      TestMessageUtil.getResourcesAsMessage(
          ListGameRequest.getDefaultInstance(),
          "com/bbuhot/server/service/user_list_game_request.json");

    listGameReplyExpected =
      TestMessageUtil.getResourcesAsMessage(
          ListGameReply.getDefaultInstance(),
          "com/bbuhot/server/service/user_list_game_reply.json");

    listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
    assertEquals(listGameReplyExpected, listGameReply);
  }
}
