package com.bbuhot.server.app;

import io.undertow.Undertow;
import javax.inject.Inject;

public class EntryPoint {

  public static AppComponent appComponent;

  private final Undertow undertow;

  @Inject
  EntryPoint(Undertow undertow) {
    this.undertow = undertow;
  }

  public static void main(final String[] args) {
    appComponent = DaggerAppComponent.create();

    appComponent.entryPoint().startServer();
  }

  private void startServer() {
    undertow.start();
  }
}
