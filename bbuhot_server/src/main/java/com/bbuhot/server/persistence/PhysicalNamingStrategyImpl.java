package com.bbuhot.server.persistence;

import com.bbuhot.server.app.EntryPoint;
import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.EntityMapping;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.Entity;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/** This class is created by reflecting. */
public class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl {

  private final Map<String, Identifier> tableMapping;
  @Inject Set<Class<?>> annotatedClasses;

  public PhysicalNamingStrategyImpl() {
    EntryPoint.appComponent.inject(this);

    tableMapping =
        generateMapping(Flags.getInstance().getDatabase().getTablePrefix(), annotatedClasses);
  }

  private static Map<String, Identifier> generateMapping(
      String tablePrefix, Set<Class<?>> annotatedClasses) {
    Map<String, Identifier> tableMapping = new HashMap<>();

    for (Class<?> annotatedClass : annotatedClasses) {
      EntityMapping entityMapping = annotatedClass.getAnnotation(EntityMapping.class);
      if (entityMapping == null) {
        continue;
      }
      String prefix = entityMapping.hasPrefix() ? tablePrefix : "";
      String entityMappingName = prefix + entityMapping.tableName();

      Entity entity = annotatedClass.getAnnotation(Entity.class);
      String annotatedName = entity == null ? "" : entity.name();
      String entityName = annotatedName.isEmpty() ? annotatedClass.getSimpleName() : annotatedName;

      System.out.println(entityMappingName + "  -> " + entityName);

      tableMapping.put(entityName, Identifier.toIdentifier(entityMappingName));
    }

    return tableMapping;
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    return tableMapping.getOrDefault(name.getText(), name);
  }
}
