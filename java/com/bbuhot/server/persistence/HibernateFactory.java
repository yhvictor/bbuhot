package com.bbuhot.server.persistence;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.Set;

class HibernateFactory {

  private final Set<Class<?>> annotatedClasses;

  @Inject
  HibernateFactory(Set<Class<?>> annotatedClasses) {
    this.annotatedClasses = annotatedClasses;
  }

  EntityManagerFactory create(Map<String, Object> properties) {
    StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
    registryBuilder.applySettings(properties);
    StandardServiceRegistry registry = registryBuilder.build();

    MetadataSources sources = new MetadataSources(registry);
    for (Class annotatedClass : annotatedClasses) {
      sources.addAnnotatedClass(annotatedClass);
    }
    Metadata metadata = sources.buildMetadata();

    return metadata.buildSessionFactory();
  }
}
