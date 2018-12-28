package com.bbuhot.server.app;

import com.bbuhot.server.entity.EntityModule;
import com.bbuhot.server.persistence.PersistenceModule;
import com.bbuhot.server.service.ServiceModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
      EntityModule.class,
      PersistenceModule.class,
      ServiceModule.class,
    })
public interface AppComponent {

  EntryPoint entryPoint();
}
