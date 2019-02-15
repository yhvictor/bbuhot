import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { DataStoreModule } from './data-store/data-store.module';
import { FacadeModule } from './facade/facade.module';
import { WarehouseModule } from './warehouse/warehouse.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    DataStoreModule,
    AuthModule,
    WarehouseModule,
    FacadeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
