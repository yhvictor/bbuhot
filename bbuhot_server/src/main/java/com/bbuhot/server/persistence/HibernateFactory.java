package com.bbuhot.server.persistence;

import com.bbuhot.server.app.Flags;
import com.mysql.cj.jdbc.Driver;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL55Dialect;

class HibernateFactory {

  private final Flags flags;
  private final Set<Class<?>> annotatedClasses;

  @Inject
  HibernateFactory(Flags flags, Set<Class<?>> annotatedClasses) {
    this.flags = flags;
    this.annotatedClasses = annotatedClasses;
  }

  EntityManagerFactory create() {
    StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
    registryBuilder.applySettings(generatePersistenceProperties());
    StandardServiceRegistry registry = registryBuilder.build();

    MetadataSources sources = new MetadataSources(registry);
    for (Class annotatedClass : annotatedClasses) {
      sources.addAnnotatedClass(annotatedClass);
    }
    Metadata metadata = sources.buildMetadata();

    return metadata.buildSessionFactory();
  }

  private Map<String, Object> generatePersistenceProperties() {
    Map<String, Object> properties = new HashMap<>();

    properties.put(AvailableSettings.DIALECT, MySQL55Dialect.class.getName());
    properties.put(
        AvailableSettings.PHYSICAL_NAMING_STRATEGY, PhysicalNamingStrategyImpl.class.getName());

    // Hibernate connection properties
    properties.put(AvailableSettings.DRIVER, Driver.class.getName());
    properties.put(AvailableSettings.URL, flags.getDatabase().getUrl());
    properties.put(AvailableSettings.USER, flags.getDatabase().getUser());
    properties.put(AvailableSettings.PASS, flags.getDatabase().getPassword());

    // Standard JPA connection properties
    // properties.put(AvailableSettings.JPA_JDBC_DRIVER, Driver.class.getName());
    // properties.put(AvailableSettings.JPA_JDBC_URL, flags.getDatabase().getUrl());
    // properties.put(AvailableSettings.JPA_JDBC_USER, flags.getDatabase().getUser());
    // properties.put(AvailableSettings.JPA_JDBC_PASSWORD, flags.getDatabase().getPassword());

    if (flags.isDebug()) {
      properties.put(AvailableSettings.SHOW_SQL, "True");
    }

    return properties;
  }
}
