package com.bbuhot.server.entity;

import com.google.common.collect.Sets;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class EntityModule {

  public static Set<Class<?>> annotatedClasses =
      Sets.newHashSet(
          // Keep alphabetical order.
          BetEntity.class,
          ExtcreditsEntity.class,
          GameEntity.BettingOptionEntity.class,
          GameEntity.class,
          UserEntity.class);

  @Provides
  @Singleton
  Set<Class<?>> annotatedClasses() {
    return annotatedClasses;
  }
}
