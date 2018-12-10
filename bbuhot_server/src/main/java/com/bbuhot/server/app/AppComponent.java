package com.bbuhot.server.app;

import com.bbuhot.server.entity.EntityMappingModule;
import com.bbuhot.server.persistence.PersistenceModule;
import com.bbuhot.server.persistence.PhysicalNamingStrategyImpl;
import com.bbuhot.server.service.ServiceModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
      EntityMappingModule.class,
      PersistenceModule.class,
      ServiceModule.class,
    })
public interface AppComponent {

  EntryPoint entryPoint();

  void inject(PhysicalNamingStrategyImpl physicalNamingStrategy);
}
