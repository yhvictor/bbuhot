package com.bbuhot.server.persistence

import com.bbuhot.server.entity.BetEntity;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class BetQueries {

    private static final String SUM_SQL = "SELECT SUM(b.betAmount) FROM BetEntity b WHERE b.gameId = ?1 AND b.uid = ?2";

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

    //TODO(luciusgone): finish this;
    public void saveBets(List<BetEntity> bets) {
    }

    //TODO(luciusgone): finish this;
    public void deleteBets(int gameId, int uid) {
    }

    //TODO(luciusgone): finish this;
    public List<BetEntity> queryByGameAndUser(int gameId, int uid) {
    }
}
