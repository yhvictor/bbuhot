package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.UserEntity;
import com.google.errorprone.annotations.RestrictedApi;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jetbrains.annotations.TestOnly;

public class UserQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  UserQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public Optional<UserEntity> queryUserById(int uid) {
    UserEntity user = entityManagerFactory.createEntityManager().find(UserEntity.class, uid);
    return Optional.ofNullable(user);
  }

  @RestrictedApi(explanation = "Test only", link = "", whitelistAnnotations = {TestOnly.class})
  public void save(UserEntity userEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    if (userEntity.getUid() > 0) {
      entityManager.merge(userEntity);
    } else {
      entityManager.persist(userEntity);
    }
    entityManager.flush();
    entityManager.getTransaction().commit();
  }
}
