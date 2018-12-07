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
  @Singleton
  Undertow undertow(Flags flags, UndertowHttpHandler undertowHttpHandler) {
    return Undertow.builder()
        .addHttpListener(flags.getPort(), "127.0.0.1")
        .setHandler(undertowHttpHandler)
        .build();
  }
}
