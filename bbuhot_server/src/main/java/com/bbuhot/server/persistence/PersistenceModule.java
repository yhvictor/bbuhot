package com.bbuhot.server.persistence;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Module
public class PersistenceModule {

  @Provides
  @Singleton
  EntityManagerFactory entityManagerFactory(HibernateFactory factory) {
    return factory.create();
  }

  @Provides
  EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory.createEntityManager();
  }
}
