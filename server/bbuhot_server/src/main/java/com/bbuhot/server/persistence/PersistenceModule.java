package com.bbuhot.server.persistence;

import com.bbuhot.server.app.Flags;
import com.mysql.cj.jdbc.Driver;
import dagger.Module;
import dagger.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL55Dialect;

@Module
public class PersistenceModule {

  private static Map<String, Object> generatePersistenceProperties() {
    Map<String, Object> properties = new HashMap<>();

    properties.put(AvailableSettings.DIALECT, MySQL55Dialect.class.getName());
    properties.put(
        AvailableSettings.PHYSICAL_NAMING_STRATEGY, PhysicalNamingStrategyImpl.class.getName());

    // Hibernate connection properties
    properties.put(AvailableSettings.DRIVER, Driver.class.getName());
    properties.put(AvailableSettings.URL, Flags.getInstance().getDatabase().getUrl());
    properties.put(AvailableSettings.USER, Flags.getInstance().getDatabase().getUser());
    properties.put(AvailableSettings.PASS, Flags.getInstance().getDatabase().getPassword());

    // Standard JPA connection properties
    // properties.put(AvailableSettings.JPA_JDBC_DRIVER, Driver.class.getName());
    // properties.put(AvailableSettings.JPA_JDBC_URL, flags.getDatabase().getUrl());
    // properties.put(AvailableSettings.JPA_JDBC_USER, flags.getDatabase().getUser());
    // properties.put(AvailableSettings.JPA_JDBC_PASSWORD, flags.getDatabase().getPassword());

    if (Flags.getInstance().isDebug()) {
      properties.put(AvailableSettings.SHOW_SQL, "True");
    }

    return properties;
  }

  @Provides
  EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
    return entityManagerFactory.createEntityManager();
  }

  @Provides
  @Singleton
  EntityManagerFactory entityManagerFactory(HibernateFactory factory) {
    return factory.create(generatePersistenceProperties());
  }
}
