package com.bbuhot.server.app;

import com.bbuhot.server.config.Flags;
import com.bbuhot.server.flyway.DatabaseController;
import io.undertow.Undertow;

import javax.inject.Inject;

public class EntryPoint {

  private final Undertow undertow;

  @Inject
  EntryPoint(Undertow undertow) {
    this.undertow = undertow;
  }

  public static void main(final String[] args) {
    // Parsing flags.
    Flags.initialize(args);

    DatabaseController.migrateDatabase(Flags.getInstance().getDatabase());

    // Start the server.
    AppComponent appComponent = DaggerAppComponent.create();
    appComponent.entryPoint().startServer();
  }

  private void startServer() {
    undertow.start();
  }
}
