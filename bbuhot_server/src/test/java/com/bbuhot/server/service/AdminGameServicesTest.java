package com.bbuhot.server.service;

import static junit.framework.TestCase.assertEquals;

import com.bbuhot.server.entity.UserEntity;
import com.bbuhot.server.persistence.UserQueries;
import com.bbuhot.server.util.TestMessageUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AdminGameServicesTest {

  @Inject AdminGameUpdatingService adminGameUpdatingService;
  @Inject AdminGameStatusChangeService adminGameStatusChangeService;

  @Inject UserQueries userQueries;
  @Inject EntityManagerFactory entityManagerFactory;

  @Before
  public void setUp() {
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
  public void tearDown() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
    entityManager.getTransaction().commit();
  }

  @Test
  public void testAdminWorkflow() {
    // Create game.
    AdminGameRequest adminGameRequest =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameRequest.getDefaultInstance(),
            "com/bbuhot/server/service/create_game_request.json");
    AdminGameReply adminGameReplyExpected =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameReply.getDefaultInstance(),
            "com/bbuhot/server/service/create_game_reply.json");

    AdminGameReply adminGameReply =
        adminGameUpdatingService.callProtobufServiceImpl(adminGameRequest);
    assertEquals(adminGameReplyExpected, adminGameReply);

    // Update game.
    adminGameRequest =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameRequest.getDefaultInstance(),
            "com/bbuhot/server/service/update_game_request.json");
    adminGameReplyExpected =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameReply.getDefaultInstance(),
            "com/bbuhot/server/service/update_game_reply.json");

    adminGameReply = adminGameUpdatingService.callProtobufServiceImpl(adminGameRequest);
    assertEquals(adminGameReplyExpected, adminGameReply);

    // Publish game.
    AdminGameStatusRequest adminGameStatusRequest =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameStatusRequest.getDefaultInstance(),
            "com/bbuhot/server/service/publish_game_request.json");
    AdminGameStatusReply adminGameStatusReplyExpected =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameStatusReply.getDefaultInstance(),
            "com/bbuhot/server/service/publish_game_reply.json");

    AdminGameStatusReply adminGameStatusReply =
        adminGameStatusChangeService.callProtobufServiceImpl(adminGameStatusRequest);
    assertEquals(adminGameStatusReplyExpected, adminGameStatusReply);

    // Settle game.
    adminGameStatusRequest =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameStatusRequest.getDefaultInstance(),
            "com/bbuhot/server/service/settle_game_request.json");
    adminGameStatusReplyExpected =
        TestMessageUtil.getResourcesAsMessage(
            AdminGameStatusReply.getDefaultInstance(),
            "com/bbuhot/server/service/settle_game_reply.json");

    adminGameStatusReply =
        adminGameStatusChangeService.callProtobufServiceImpl(adminGameStatusRequest);
    assertEquals(adminGameStatusReplyExpected, adminGameStatusReply);
  }
}
