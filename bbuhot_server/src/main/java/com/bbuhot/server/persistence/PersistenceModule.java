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
    properties.put(AvailableSettings.URL, Flags.getInstance().getDatabase().getConnectionUrl());
    properties.put(AvailableSettings.USER, Flags.getInstance().getDatabase().getUser());
    properties.put(AvailableSettings.PASS, Flags.getInstance().getDatabase().getPassword());

    // Optimize
    properties.put(AvailableSettings.ORDER_INSERTS, true);
    properties.put(AvailableSettings.ORDER_UPDATES, true);
    properties.put(AvailableSettings.C3P0_MIN_SIZE, 5);
    properties.put(AvailableSettings.C3P0_MAX_SIZE, 20);
    properties.put(AvailableSettings.C3P0_TIMEOUT, 1800);
    properties.put(AvailableSettings.C3P0_MAX_STATEMENTS, 50);

    // Standard JPA connection properties
    // properties.put(AvailableSettings.JPA_JDBC_DRIVER, Driver.class.getName());
    // properties.put(AvailableSettings.JPA_JDBC_URL, flags.getDatabase().getUrl());
    // properties.put(AvailableSettings.JPA_JDBC_USER, flags.getDatabase().getUser());
    // properties.put(AvailableSettings.JPA_JDBC_PASSWORD, flags.getDatabase().getPassword());

    if (Flags.isDebug()) {
      properties.put(AvailableSettings.SHOW_SQL, true);
      properties.put(AvailableSettings.FORMAT_SQL, true);
      // properties.put(AvailableSettings.GENERATE_STATISTICS, true);
    } else {
      // TODO(yhvictor): enable.
      // properties.put(AvailableSettings.USE_QUERY_CACHE, true);
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
