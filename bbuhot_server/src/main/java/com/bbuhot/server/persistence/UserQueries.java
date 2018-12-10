package com.bbuhot.server.persistence;

import com.bbuhot.server.entity.User;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class UserQueries {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  UserQueries(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public Optional<User> queryUserById(int uid) {
    List<?> userList =
        entityManagerFactory
            .createEntityManager()
            .createQuery("From User u where u.uid = ?1")
            .setParameter(1, uid)
            .getResultList();

    if (userList.size() > 1) {
      throw new IllegalStateException("Too many results.");
    }
    return userList.stream().findFirst().map(object -> (User) object);
  }
}
