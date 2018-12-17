package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.GameEntity;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GameQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  GameQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public void create(GameEntity gameEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(gameEntity);
    entityManager.flush();
    entityManager.getTransaction().commit();
  }

  public void update(GameEntity gameEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    entityManager.merge(gameEntity);
    entityManager.flush();
    entityManager.getTransaction().commit();
  }
}
