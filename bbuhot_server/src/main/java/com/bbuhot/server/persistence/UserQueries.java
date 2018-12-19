package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.UserEntity;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class UserQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  UserQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public Optional<UserEntity> queryUserById(int uid) {
    UserEntity user =
        entityManagerFactory.createEntityManager().find(UserEntity.class, uid);
    return Optional.of(user);
  }
}
