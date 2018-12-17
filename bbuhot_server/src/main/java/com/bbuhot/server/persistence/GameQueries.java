package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.GameEntity;
import java.util.List;
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

  public List<GameEntity> queryByStatus(int gameStatus) {
    @SuppressWarnings("unchecked")
    List<GameEntity> gameList =
        entityManagerFactory
            .createEntityManager()
            .createQuery("From GameEntity g where g.status = ?1")
            .setParameter(1, gameStatus)
            .getResultList();

    return gameList;
  }
}
