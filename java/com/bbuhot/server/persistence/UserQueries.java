package com.bbuhot.server.persistence;

import com.bbuhot.errorprone.TestOnly;
import com.bbuhot.server.entity.UserEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

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

  @TestOnly
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
