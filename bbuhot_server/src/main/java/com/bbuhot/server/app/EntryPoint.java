package com.bbuhot.server.app;

import com.bbuhot.server.config.Configuration.Database;
import io.undertow.Undertow;
import javax.inject.Inject;
import org.flywaydb.core.Flyway;

public class EntryPoint {

  public static AppComponent appComponent;
  private final Undertow undertow;

  @Inject
  EntryPoint(Undertow undertow) {
    this.undertow = undertow;
  }

  public static void main(final String[] args) {
    // Parsing flags.
    Flags.initialize(args);

    migrateDatabase();

    // Start the server.
    appComponent = DaggerAppComponent.create();
    appComponent.entryPoint().startServer();
  }

  private void startServer() {
    undertow.start();
  }

  private static void migrateDatabase() {
    Database database = Flags.getInstance().getDatabase();
    Flyway flyway =
        Flyway.configure()
            .dataSource(database.getConnectionUrl(), database.getUser(), database.getPassword())
            .locations("com.bbuhot.server.db")
            .ignoreMissingMigrations(true)
            .baselineOnMigrate(true)
            .load();

    flyway.migrate();
  }
}
