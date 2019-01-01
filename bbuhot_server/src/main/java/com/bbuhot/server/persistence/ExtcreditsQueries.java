package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.ExtcreditsEntity;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ExtcreditsQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  ExtcreditsQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public Optional<ExtcreditsEntity> queryById(int uid) {
    ExtcreditsEntity extcreditsEntity = entityManagerFactory
        .createEntityManager()
        .find(ExtcreditsEntity.class, uid);

    return Optional.ofNullable(extcreditsEntity);
  }

  public void save(ExtcreditsEntity extcreditsEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    if (extcreditsEntity.getUid() > 0) {
      entityManager.merge(extcreditsEntity);
    } else {
      entityManager.persist(extcreditsEntity);
    }
    entityManager.getTransaction().commit();
  }
}
