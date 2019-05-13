package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.GameEntity;
import com.bbuhot.server.service.Game;
import com.bbuhot.server.service.Game.Status;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class GameQueries {

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

  public List<GameEntity> queryByStatus(GameEntityStatus gameEntityStatus, boolean isAdmin) {
    CriteriaBuilder builder = entityManagerFactory.getCriteriaBuilder();
    CriteriaQuery<GameEntity> criteria = builder.createQuery(GameEntity.class);
    Root<GameEntity> root = criteria.from(GameEntity.class);
    criteria.select(root);

    if (isAdmin) {
      criteria.where(builder.equal(root.get("status"), gameEntityStatus.value));
    } else {
      criteria.where(builder.and(
              builder.equal(root.get("normalUserVisible"), true),
              builder.equal(root.get("status"), gameEntityStatus.value)));
    }

    List<GameEntity> gameList =
        entityManagerFactory
            .createEntityManager()
            .createQuery(criteria)
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
