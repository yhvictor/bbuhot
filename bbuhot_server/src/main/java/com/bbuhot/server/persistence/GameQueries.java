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

  private static final String LIST_SQL = "Select g From GameEntity g where ";

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  GameQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public void save(GameEntity gameEntity) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    if (gameEntity.getId() > 0) {
      entityManager.merge(gameEntity);
    } else {
      entityManager.persist(gameEntity);
    }
    entityManager.getTransaction().commit();
  }

  public Optional<GameEntity> queryById(int id) {
    GameEntity gameEntity = entityManagerFactory.createEntityManager().find(GameEntity.class, id);
    return Optional.ofNullable(gameEntity);
  }

  public List<GameEntity> queryByStatus(GameEntityStatus gameEntityStatus) {
    @SuppressWarnings("unchecked")
    List<GameEntity> gameList =
        entityManagerFactory
            .createEntityManager()
            .createQuery(LIST_SQL + "g.status = ?1")
            .setParameter(1, gameEntityStatus.value)
            .getResultList();

    return gameList;
  }

  public enum GameEntityStatus {
    DRAFT(0, Game.Status.DRAFT),
    PUBLISHED(1, Status.PUBLISHED),
    SETTLED(2, Status.SETTLED),
    CANCELLED(3, Status.CANCELLED),
    ;
    public final int value;
    public final Game.Status serviceStatus;

    GameEntityStatus(int value, Game.Status serviceStatus) {
      this.value = value;
      this.serviceStatus = serviceStatus;
    }

    public static GameEntityStatus valueOf(int value) {
      for (GameEntityStatus status : GameEntityStatus.values()) {
        if (status.value == value) {
          return status;
        }
      }

      throw new IllegalStateException("Wrong mapping value");
    }

    public static GameEntityStatus valueOf(Game.Status value) {
      for (GameEntityStatus status : GameEntityStatus.values()) {
        if (status.serviceStatus == value) {
          return status;
        }
      }

      throw new IllegalStateException("Wrong mapping value");
    }
  }
}
