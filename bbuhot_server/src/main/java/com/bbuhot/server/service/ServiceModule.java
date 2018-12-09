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
  @Singleton
  Undertow undertow(UndertowHttpHandler undertowHttpHandler) {
    return Undertow.builder()
        .addHttpListener(Flags.getInstance().getPort(), "0.0.0.0")
        .setHandler(undertowHttpHandler)
        .build();
  }
}
