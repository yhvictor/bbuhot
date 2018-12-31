package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.BetEntity;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BetQueries {

  private static final String SUM_SQL = "SELECT SUM(b.betAmount) FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";
  private static final String DELETE_SQL = "DELETE FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";
  private static final String SELECT_SQL = "SELECT b FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  BetQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public int queryBetted(int gameId, int uid) {
    int betted = (int) entityManagerFactory
        .createEntityManager()
        .createQuery(SUM_SQL)
        .setParameter(1, gameId)
        .setParameter(2, uid)
        .getSingleResult();

    return betted;
  }

  public void saveBets(List<BetEntity> betEntities, int gameId, int uid) {
      deleteBets(gameId, uid);

      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      for (BetEntity betEntity:betEntities) {
          entityManager.persist(betEntity);
      }
      entityManager.getTransaction().commit();
  }

  public void deleteBets(int gameId, int uid) {
    entityManagerFactory
        .createEntityManager()
        .createQuery(DELETE_SQL)
        .setParameter(1, gameId)
        .setParameter(2, uid)
        .executeUpdate();
  }

  public List<BetEntity> queryByGameAndUser(int gameId, int uid) {
    List<BetEntity> betEntities = entityManagerFactory
        .createEntityManager()
        .createQuery(SELECT_SQL)
        .setParameter(1, gameId)
        .setParameter(2, uid)
        .getResultList();

    return betEntities;
  }
}
