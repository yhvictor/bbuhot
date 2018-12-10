package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.User;
import java.util.List;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class UserQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  UserQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Nullable
  public User queryUserById(int uid) {
    List userList =
        entityManagerFactory
            .createEntityManager()
            .createQuery("From User u where u.uid = ?1")
            .setParameter(1, uid)
            .getResultList();

    if (userList.isEmpty()) {
      return null;
    }
    if (userList.size() > 1) {
      throw new IllegalStateException("Too many results.");
    }

    return (User) userList.get(0);
  }
}
