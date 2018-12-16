package com.bbuhot.server.persistence;

import com.bbuhot.server.app.EntryPoint;
import com.bbuhot.server.app.Flags;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.Table;
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
      Table table = annotatedClass.getAnnotation(Table.class);
      String tableName = table.name();
      String modifiedTableName =
          tableName.replace("{pre}", Flags.getInstance().getDatabase().getTablePrefix());

      if (tableName.equals(modifiedTableName)) {
        continue;
      }

      if (Flags.getInstance().isDebug()) {
        System.out.println(tableName + " -> " + modifiedTableName);
      }

      tableMapping.put(tableName, Identifier.toIdentifier(modifiedTableName));
    }

    return tableMapping;
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    return tableMapping.getOrDefault(name.getText(), name);
  }
}
