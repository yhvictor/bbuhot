import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DataStoreModule } from './data-store/data-store.module';
import { WarehouseModule } from './warehouse/warehouse.module';
import { FacadeModule } from './facade/facade.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, AppRoutingModule, DataStoreModule, WarehouseModule, FacadeModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
