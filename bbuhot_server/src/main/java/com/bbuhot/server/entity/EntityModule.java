package com.bbuhot.server.entity;

import com.google.common.collect.Sets;
import dagger.Module;
import dagger.Provides;
import java.util.Set;
import javax.inject.Singleton;

@Module
public class EntityModule {

  @Provides
  @Singleton
  Set<Class<?>> annotatedClasses() {
    return Sets.newHashSet(
        // Keep alphabetical order.
        UserEntity.class);
  }
}
