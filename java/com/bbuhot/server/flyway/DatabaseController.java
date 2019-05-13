package com.bbuhot.server.flyway;

import com.bbuhot.server.config.Configuration;
import org.flywaydb.core.Flyway;

public class DatabaseController {

    public static void migrateDatabase(Configuration.Database database) {
        Flyway flyway =
                Flyway.configure()
                        .dataSource(database.getConnectionUrl(), database.getUser(), database.getPassword())
                        .locations("com.bbuhot.server.flyway.db")
                        .ignoreMissingMigrations(true)
                        .baselineOnMigrate(true)
                        .load();

        flyway.migrate();
    }
}
