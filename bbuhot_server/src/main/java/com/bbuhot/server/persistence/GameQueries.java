package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.service.Game;
import com.bbuhot.server.service.Game.Status;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GameQueries {

  private static final String LIST_SQL = "Select g From GameEntity g join fetch g.betEntities b "
      + "where ";

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

  public Optional<GameEntity> queryById(int id) {
    List<?> gameList =
        entityManagerFactory
            .createEntityManager()
            .createQuery(LIST_SQL + "g.id = ?1")
            .setParameter(1, id)
            .getResultList();

    if (gameList.size() > 1) {
      throw new IllegalStateException("Too many results.");
    }
    return gameList.stream().findFirst().map(object -> (GameEntity) object);
  }

  public List<GameEntity> queryByStatus(GameStatus gameStatus) {
    @SuppressWarnings("unchecked")
    List<GameEntity> gameList =
        entityManagerFactory
            .createEntityManager()
            .createQuery(LIST_SQL + "g.status = ?1")
            .setParameter(1, gameStatus.value)
            .getResultList();

    return gameList;
  }

  public enum GameStatus {
    DRAFT(0, Game.Status.DRAFT),
    PUBLISHED(1, Status.PUBLISHED),
    SETTLED(2, Status.SETTLED),
    CANCELLED(3, Status.CANCELLED),
    ;
    public int value;
    public Game.Status serviceStatus;

    GameStatus(int value, Game.Status serviceStatus) {
      this.value = value;
      this.serviceStatus = serviceStatus;
    }

    public static GameStatus valueOf(int value) {
      for (GameStatus status : GameStatus.values()) {
        if (status.value == value) {
          return status;
        }
      }

      throw new IllegalStateException("Wrong mapping value");
    }

    public static GameStatus valueOf(Game.Status value) {
      for (GameStatus status : GameStatus.values()) {
        if (status.serviceStatus == value) {
          return status;
        }
      }

      throw new IllegalStateException("Wrong mapping value");
    }
  }
}
