package com.bbuhot.server.service;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class GameListingServiceTest {

    @Inject
    ListGameService listGameService;

    @Inject
    BetQueries betQueries;
    @Inject
    GameQueries gameQueries;
    @Inject
    UserQueries userQueries;
    @Inject
    EntityManagerFactory entityManagerFactory;

    private TestMessageUtil testMessageUtil;

    @Before
    public void setUp() {
        testMessageUtil = new TestMessageUtil("com/bbuhot/server/service/res/");
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        TestServiceComponent.getInstance().inject(this);

        // create user
        UserEntity user = new UserEntity();
        user.setUid(1);
        user.setUsername("admin");
        user.setGroupId(1);
        user.setPassword("b2b29e4862ffe7a5d1388d1722a9aae5");
        userQueries.save(user);

        // create games
        // draft game
        createGame(1, /* visible= */ false, GameEntityStatus.DRAFT);

        // published game
        GameEntity game = createGame(2, /* visible= */ false, GameEntityStatus.PUBLISHED);
        createBet(1, user.getUid(), game.getId(), BetEntityStatus.UNSETTLED);
        game = createGame(3, /* visible= */ true, GameEntityStatus.PUBLISHED);
        createBet(2, user.getUid(), game.getId(), BetEntityStatus.UNSETTLED);

        // settled game
        game = createGame(4, /* visible= */ true, GameEntityStatus.SETTLED);
        createBet(3, user.getUid(), game.getId(), BetEntityStatus.SETTLED);

        // cancelled game
        game = createGame(5, /* visible= */ true, GameEntityStatus.CANCELLED);
        createBet(4, user.getUid(), game.getId(), BetEntityStatus.CANCELLED);
    }

    private GameEntity createGame(int id, boolean visible, GameEntityStatus status) {
        GameEntity game = new GameEntity();
        game.setId(id);
        game.setName("Ti8 Grand final LGD vs OG");
        game.setDescription("Ti8 Grand final LGD vs OG");
        game.setNormalUserVisible(visible);
        game.setStatus(status.value);
        game.setBetOptionLimit(1);
        game.setBetAmountLowest(100);
        game.setBetAmountHighest(400);
        game.setEndTimeMs(new Timestamp(1535126400000L));
        if (status == GameEntityStatus.SETTLED) {
            game.setWinningBetOption(0);
        } else {
            game.setWinningBetOption(-2);
        }
        BettingOptionEntity option1 = new BettingOptionEntity();
        option1.setName("LGD win");
        option1.setOdds(2000000);
        game.getBettingOptionEntities().add(option1);
        BettingOptionEntity option2 = new BettingOptionEntity();
        option2.setName("OG win");
        option2.setOdds(8000000);
        game.getBettingOptionEntities().add(option2);
        gameQueries.save(game);

        return game;
    }

    private void createBet(int id, int uid, int gameId, BetEntityStatus status) {
        BetEntity bet = new BetEntity();
        bet.setId(id);
        bet.setUid(uid);
        bet.setGameId(gameId);
        bet.setBettingOptionId(0);
        bet.setBetAmount(100);
        bet.setStatus(status.value);
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
    public void testAdminDraftGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "admin_list_draft_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "admin_list_draft_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testAdminPublishedGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "admin_list_published_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "admin_list_published_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testAdminSettledGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "admin_list_settled_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "admin_list_settled_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testAdminCancelledGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "admin_list_cancelled_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "admin_list_cancelled_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testUserPublisedGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "user_list_published_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "user_list_published_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testUserSettledGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "user_list_settled_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "user_list_settled_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }

    @Test
    public void testUserCancelledGameListing() {
        ListGameRequest listGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        ListGameRequest.getDefaultInstance(), "user_list_cancelled_game_request.json");

        ListGameReply listGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        ListGameReply.getDefaultInstance(), "user_list_cancelled_game_reply.json");

        ListGameReply listGameReply = listGameService.callProtobufServiceImpl(listGameRequest);
        assertEquals(listGameReplyExpected, listGameReply);
    }
}
