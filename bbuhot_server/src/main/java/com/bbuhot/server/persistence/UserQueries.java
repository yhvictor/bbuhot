package com.bbuhot.server.persistence;

import com.bbuhot.errorprone.TestOnly;
import com.bbuhot.server.entity.UserEntity;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

  public int queryRemainingMoney(int uid) {
    UserEntity user = entityManagerFactory.createEntityManager().find(UserEntity.class, uid);
    return user.getExcreditsEntity().getExtcredits2();
  }

  public void updateRemainingMoney(int uid, int remainingMoney) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    UserEntity user = entityManager.find(UserEntity.class, uid);
    user.getExcreditsEntity.setExtcredits2(remainingMoney);
    entityManager.merge(user);
    entityManager.getTransaction().commit();
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
