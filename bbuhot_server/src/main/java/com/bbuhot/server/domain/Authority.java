package com.bbuhot.server.domain;

import com.bbuhot.server.entity.User;
import com.bbuhot.server.service.UserDto;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

public class Authority {

  private final EntityManagerFactory entityManagerFactory;

  @Inject
  Authority(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  public UserDto testWorkflow(int uid) {
    User user =
        (User)
            entityManagerFactory
                .createEntityManager()
                .createQuery("From User u where u.uid = ?1")
                .setParameter(1, uid)
                .getSingleResult();
    return UserDto.newBuilder().setId(user.getUid()).setName(user.getUserName()).build();
  }
}
