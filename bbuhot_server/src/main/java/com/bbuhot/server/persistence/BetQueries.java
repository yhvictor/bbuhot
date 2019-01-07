package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.BetEntity;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BetQueries {

  private static final String SUM_SQL =
      "SELECT SUM(b.betAmount) FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";
  private static final String DELETE_SQL =
      "DELETE FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";
  private static final String SELECT_SQL =
      "SELECT b FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";
  private static final String UPDATE_EXTCREDITS2_SQL =
      "UPDATE ExtcreditsEntity e SET e.extcredits2 = e.extcredits2 + (?1) WHERE e.uid = ?2";

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  BetQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public int queryBetted(int gameId, int uid) {
    Object result =
        entityManagerFactory
            .createEntityManager()
            .createQuery(SUM_SQL)
            .setParameter(1, gameId)
            .setParameter(2, uid)
            .getSingleResult();

    if (result == null) return 0;

    // TODO(luciusgone): refactoring code?
    return ((Long) result).intValue();
  }

  public void saveBets(List<BetEntity> betEntities, int gameId, int uid, int increment) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    // delete all bets
    entityManager
        .createQuery(DELETE_SQL)
        .setParameter(1, gameId)
        .setParameter(2, uid)
        .executeUpdate();
    // add all bets
    for (BetEntity betEntity : betEntities) {
      entityManager.persist(betEntity);
    }
    // update credits
    entityManager
        .createQuery(UPDATE_EXTCREDITS2_SQL)
        .setParameter(1, increment)
        .setParameter(2, uid)
        .executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void deleteBets(int gameId, int uid, int increment) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    // delete all bets
    entityManager
        .createQuery(DELETE_SQL)
        .setParameter(1, gameId)
        .setParameter(2, uid)
        .executeUpdate();
    // update credits
    entityManager
        .createQuery(UPDATE_EXTCREDITS2_SQL)
        .setParameter(1, increment)
        .setParameter(2, uid)
        .executeUpdate();
    entityManager.getTransaction().commit();
  }

  public List<BetEntity> queryByGameAndUser(int gameId, int uid) {
    @SuppressWarnings("unchecked")
    List<BetEntity> betEntities =
        entityManagerFactory
            .createEntityManager()
            .createQuery(SELECT_SQL)
            .setParameter(1, gameId)
            .setParameter(2, uid)
            .getResultList();

    return betEntities;
  }
}
