package com.bbuhot.server.persistence;

import dagger.Module;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.tool.schema.Action;

@Module
public class TestPersistenceModule extends PersistenceModule {

  /** Creates connection for in memory DB. */
  @Override
  protected Map<String, Object> generateConnectionProperties() {
    Map<String, Object> properties = new HashMap<>();

    properties.put(AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName());
    properties.put(AvailableSettings.DRIVER, org.h2.Driver.class.getName());
    properties.put(AvailableSettings.URL, "jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1");
    properties.put(AvailableSettings.HBM2DDL_AUTO, Action.CREATE);

    return properties;
  }
}
