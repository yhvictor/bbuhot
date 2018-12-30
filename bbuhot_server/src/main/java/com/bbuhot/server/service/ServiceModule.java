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
  HttpHandler adminGameUpdatingService(AdminGameUpdatingService adminGameUpdatingService) {
    return adminGameUpdatingService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/bet/admin_status")
  HttpHandler adminGameStatusChangeService(
      AdminGameStatusChangeService adminGameStatusChangeService) {
    return adminGameStatusChangeService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/bet/list_game")
  HttpHandler listGameService(ListGameService listGameService) {
    return listGameService;
  }

  @Provides
  @IntoMap
  @StringKey("/api/bet/bet")
  HttpHandler betUpdatingService(BetUpdatingService betUpdatingService) {
    return betUpdatingService;
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
