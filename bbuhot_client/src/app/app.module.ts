import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { ApiModule } from './api/api.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BetPageModule } from './bet-page/bet-page.module';
import { ComponentsModule } from './components/components.module';

@NgModule({
  declarations: [AppComponent],
  imports: [ApiModule, BrowserModule, AppRoutingModule, ComponentsModule, BetPageModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
