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
  private static final String SELECT_SQL = "SELECT b FROM BetEntity b WHERE b.gameId = :game_id";
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
    doDeleteBets(entityManager, gameId, uid);
    // add all bets
    for (BetEntity betEntity : betEntities) {
      entityManager.persist(betEntity);
    }
    // update credits
    doUpdateExtcredits2(entityManager, uid, increment);
    entityManager.getTransaction().commit();
  }

  public void deleteBets(int gameId, int uid, int increment) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    // delete all bets
    doDeleteBets(entityManager, gameId, uid);
    // update credits
    doUpdateExtcredits2(entityManager, uid, increment);
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
    updateBetsForGame(gameId, winningOptionId, odds, BetEntityStatus.SETTLED);
  }

  public void revokeAllRewards(int gameId) {
    updateBetsForGame(gameId, /* winningOptionId= */ 0, /* odds= */ 0, BetEntityStatus.UNSETTLED);
  }

  public void revokeAllBets(int gameId) {
    updateBetsForGame(gameId, /* winningOptionId= */ 0, /* odds= */ 0, BetEntityStatus.CANCELLED);
  }

  private void doDeleteBets(EntityManager entityManager, int gameId, int uid) {
    entityManager
        .createQuery(DELETE_SQL)
        .setParameter("game_id", gameId)
        .setParameter("uid", uid)
        .executeUpdate();
  }

  private void doUpdateExtcredits2(EntityManager entityManager, int uid, int increment) {
    entityManager
        .createQuery(UPDATE_EXTCREDITS2_SQL)
        .setParameter("increment", increment)
        .setParameter("uid", uid)
        .executeUpdate();
  }

  //                              | Revoke    | SETTLE  | CANCEL    |
  // -----------------------------+-----------+---------+-----------+
  // BetEntity.status             | UNSETTLED | SETTLED | CANCELLED |
  // BetEntity.earning            | 0         | +/-     | 0         |
  // ExtcreditsEntity.extcredits2 | -/0       | +/0     | +         |
  // -----------------------------+-----------+---------+-----------+
  private void updateBetsForGame(
      int gameId, int winningOptionId, int odds, BetEntityStatus newStatus) {
    EntityManager em1 = entityManagerFactory.createEntityManager();
    EntityManager em2 = entityManagerFactory.createEntityManager();
    Session session = em1.unwrap(Session.class);
    int increment;
    BetEntityStatus oldStatus;

    ScrollableResults cur =
        session
            .createQuery(SELECT_SQL)
            .setParameter("game_id", gameId)
            .scroll(ScrollMode.FORWARD_ONLY);

    while (cur.next()) {
      em2.getTransaction().begin();
      increment = 0;
      BetEntity bet = (BetEntity) cur.get(0);

      oldStatus = BetEntityStatus.valueOf(bet.getStatus());

      if (oldStatus == newStatus) continue;

      // set the new status
      bet.setStatus(newStatus.value);

      // set earning and credit increment
      switch (newStatus) {
        case UNSETTLED:
          if (bet.getEarning() > 0) {
            increment = -bet.getEarning();
          }
          bet.setEarning(0);
          break;
        case SETTLED:
          if (bet.getBettingOptionId() == winningOptionId) {
            increment = (int) (odds / 1000000.0 * bet.getBetAmount());
            bet.setEarning(increment);
          } else {
            bet.setEarning(-bet.getBetAmount());
          }
          break;
        case CANCELLED:
          increment = bet.getBetAmount();
          bet.setEarning(0);
          break;
        default:
          throw new IllegalStateException("Internal Error. Wrong BetEntity status");
      }

      // save bets and update user's credit
      em2.merge(bet);
      if (increment != 0) {
        doUpdateExtcredits2(em2, bet.getUid(), increment);
      }
      em2.getTransaction().commit();
    }
    session.disconnect();
  }

  public enum BetEntityStatus {
    UNSETTLED(0),
    SETTLED(1),
    CANCELLED(2),
    ;
    public final int value;

    BetEntityStatus(int value) {
      this.value = value;
    }

    public static BetEntityStatus valueOf(int value) {
      for (BetEntityStatus status : BetEntityStatus.values()) {
        if (status.value == value) {
          return status;
        }
      }

      throw new IllegalStateException("Wrong bet status mapping value");
    }
  }
}
