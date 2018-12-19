package com.bbuhot.server.persistence;

import com.bbuhot.server.app.Flags;
import com.bbuhot.server.entity.EntityModule;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Table;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/** This class is created by reflecting. */
public class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl {

  private static final Map<String, Identifier> tableMapping =
      generateMapping(Flags.getInstance().getDatabase().getTablePrefix());

  private static Map<String, Identifier> generateMapping(String tablePrefix) {
    Map<String, Identifier> tableMapping = new HashMap<>();

    for (Class<?> annotatedClass : EntityModule.annotatedClasses) {
      Table table = annotatedClass.getAnnotation(Table.class);
      String tableName = table.name();
      String modifiedTableName = tableName.replace("{pre}", tablePrefix);

      if (tableName.equals(modifiedTableName)) {
        continue;
      }

      if (Flags.isDebug()) {
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
