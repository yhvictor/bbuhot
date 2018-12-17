package com.bbuhot.server.service;

import com.bbuhot.server.app.Flags;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import javax.inject.Singleton;

@Module
public class ServiceModule {

  @Provides
  @IntoMap
  @StringKey("/api/user")
  HttpHandler userProtobufService(UserProtobufService userProtobufService) {
    return userProtobufService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/auth")
  HttpHandler authorityProtobufService(AuthorityProtobufService authorityProtobufService) {
    return authorityProtobufService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/bet/admin_game")
  HttpHandler gameUpdatingService(GameUpdatingService gameUpdatingService) {
    return gameUpdatingService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/bet/list_game")
  HttpHandler listGameService(ListGameService listGameService) {
    return listGameService;
  }

  @Provides
  @Singleton
  Undertow undertow(UndertowHttpHandler undertowHttpHandler) {
    return Undertow.builder()
        .addHttpListener(Flags.getInstance().getPort(), Flags.getInstance().getHost())
        .setHandler(undertowHttpHandler)
        .setWorkerThreads(1) // We are not using undertow work thread pool.
        .build();
  }
}
