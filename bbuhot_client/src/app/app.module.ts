import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    SpinachItemsComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
