package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.BetEntity;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;
import org.hibernate.Transaction;

public class BetQueries {

  private static final String SUM_SQL =
      "SELECT SUM(b.betAmount) FROM BetEntity b WHERE b.gameId = :game_id AND b.uid = :uid";
  private static final String DELETE_SQL =
      "DELETE FROM BetEntity b WHERE b.gameId = :game_id AND b.uid = :uid";
  private static final String SELECT_SQL =
      "SELECT b FROM BetEntity b WHERE b.gameId = :game_id";
  private static final String UPDATE_EXTCREDITS2_SQL =
      "UPDATE ExtcreditsEntity e SET e.extcredits2 = e.extcredits2 + (:increment) WHERE e.uid = :uid";

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
            .setParameter("game_id", gameId)
            .setParameter("uid", uid)
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
        .setParameter("game_id", gameId)
        .setParameter("uid", uid)
        .executeUpdate();
    // add all bets
    for (BetEntity betEntity : betEntities) {
      entityManager.persist(betEntity);
    }
    // update credits
    entityManager
        .createQuery(UPDATE_EXTCREDITS2_SQL)
        .setParameter("increment", increment)
        .setParameter("uid", uid)
        .executeUpdate();
    entityManager.getTransaction().commit();
  }

  public void deleteBets(int gameId, int uid, int increment) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    // delete all bets
    entityManager
        .createQuery(DELETE_SQL)
        .setParameter("game_id", gameId)
        .setParameter("uid", uid)
        .executeUpdate();
    // update credits
    entityManager
        .createQuery(UPDATE_EXTCREDITS2_SQL)
        .setParameter("increment", increment)
        .setParameter("uid", uid)
        .executeUpdate();
    entityManager.getTransaction().commit();
  }

  public List<BetEntity> queryByGameAndUser(int gameId, int uid) {
    @SuppressWarnings("unchecked")
    List<BetEntity> betEntities =
        entityManagerFactory
            .createEntityManager()
            .createQuery(SELECT_SQL + " AND b.uid = :uid")
            .setParameter("game_id", gameId)
            .setParameter("uid", uid)
            .getResultList();

    return betEntities;
  }

  public void rewardAllBets(int gameId, int winningOptionId, int odds) {
    EntityManager em = entityManagerFactory.createEntityManager();
    Session session = em.unwrap(Session.class);
    Transaction tx = session.beginTransaction();

    ScrollableResults cur =
        session
            .createQuery(SELECT_SQL)
            .setParameter("game_id", gameId)
            .scroll(ScrollMode.FORWARD_ONLY);

    int count = 0;

    while (cur.next()) {
      count++;
      BetEntity bet = (BetEntity) cur.get(0);

      // skip already settled bets
      if (bet.isSettled() && bet.getEarning() != 0) continue;

      // set earning
      if (bet.getBettingOptionId() == winningOptionId) {
        int earning = (int) (odds / 1000000.0 * bet.getBetAmount());
        bet.setEarning(earning);
      } else {
        bet.setEarning(-bet.getBetAmount());
      }

      // set the status to settled
      bet.setSettled(true);

      // save bets and update user's credit
      session.merge(bet);
      if (bet.getEarning() > 0) {
        session
            .createQuery(UPDATE_EXTCREDITS2_SQL)
            .setParameter("increment", bet.getEarning())
            .setParameter("uid", bet.getUid())
            .executeUpdate();
      }

      if (count % 50 == 0) {
        session.flush();
        session.clear();
      }
    }
    session.flush();

    tx.commit();
  }

  public void revokeAllRewards(int gameId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    Session session = em.unwrap(Session.class);
    Transaction tx = session.beginTransaction();

    ScrollableResults cur =
        session
            .createQuery(SELECT_SQL)
            .setParameter("game_id", gameId)
            .scroll(ScrollMode.FORWARD_ONLY);

    int count = 0;

    while (cur.next()) {
      count++;
      BetEntity bet = (BetEntity) cur.get(0);

      // skip already settled bets
      if (!bet.isSettled() && bet.getEarning() == 0) continue;

      // set earning
      int earning = bet.getEarning();
      bet.setEarning(0);

      // set the status to unsettled
      bet.setSettled(false);

      // save bets and update user's credit
      session.merge(bet);
      if (earning > 0) {
        session
            .createQuery(UPDATE_EXTCREDITS2_SQL)
            .setParameter("increment", -earning)
            .setParameter("uid", bet.getUid())
            .executeUpdate();
      }

      if (count % 50 == 0) {
        session.flush();
        session.clear();
      }
    }
    session.flush();

    tx.commit();
  }
}
