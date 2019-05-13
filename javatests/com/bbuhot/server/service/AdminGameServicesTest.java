package com.bbuhot.server.service;

import com.bbuhot.server.domain.GameStatusChanging;
import com.bbuhot.server.entity.UserEntity;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import static junit.framework.TestCase.assertEquals;

@RunWith(JUnit4.class)
public class AdminGameServicesTest {

    @Inject
    AdminGameUpdatingService adminGameUpdatingService;
    @Inject
    AdminGameStatusChangeService adminGameStatusChangeService;

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

        UserEntity user = new UserEntity();
        user.setUid(1);
        user.setUsername("admin");
        user.setGroupId(1);
        user.setPassword("b2b29e4862ffe7a5d1388d1722a9aae5");
        userQueries.save(user);
    }

    @After
    public void tearDown() throws Exception {
        // wait for releasing lock before tear down the persistence context
        while (GameStatusChanging.isGameLocked(1)) {
            Thread.sleep(500);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    public void testAdminWorkflow() {
        // Create game.
        AdminGameRequest adminGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameRequest.getDefaultInstance(), "create_game_request.json");
        AdminGameReply adminGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameReply.getDefaultInstance(), "create_game_reply.json");

        AdminGameReply adminGameReply =
                adminGameUpdatingService.callProtobufServiceImpl(adminGameRequest);
        assertEquals(adminGameReplyExpected, adminGameReply);

        // Update game.
        adminGameRequest =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameRequest.getDefaultInstance(), "update_game_request.json");
        adminGameReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameReply.getDefaultInstance(), "update_game_reply.json");

        adminGameReply = adminGameUpdatingService.callProtobufServiceImpl(adminGameRequest);
        assertEquals(adminGameReplyExpected, adminGameReply);

        // Publish game.
        AdminGameStatusRequest adminGameStatusRequest =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameStatusRequest.getDefaultInstance(), "publish_game_request.json");
        AdminGameStatusReply adminGameStatusReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameStatusReply.getDefaultInstance(), "publish_game_reply.json");

        AdminGameStatusReply adminGameStatusReply =
                adminGameStatusChangeService.callProtobufServiceImpl(adminGameStatusRequest);
        assertEquals(adminGameStatusReplyExpected, adminGameStatusReply);

        // Settle game.
        adminGameStatusRequest =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameStatusRequest.getDefaultInstance(), "settle_game_request.json");
        adminGameStatusReplyExpected =
                testMessageUtil.getResourcesAsMessage(
                        AdminGameStatusReply.getDefaultInstance(), "settle_game_reply.json");

        adminGameStatusReply =
                adminGameStatusChangeService.callProtobufServiceImpl(adminGameStatusRequest);
        assertEquals(adminGameStatusReplyExpected, adminGameStatusReply);
    }
}
