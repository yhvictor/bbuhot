import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';
import { NgZorroAntdModule, NZ_I18N, zh_CN } from 'ng-zorro-antd';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';

import { AppRoutingModule } from './app-routing.module';
import { CompetitionComponent } from './competition/competition.component';
import { CompetitionPlayerComponent } from './competition-player/competition-player.component';
import { CompetitionDetailComponent } from './competition-detail/competition-detail.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { CompetitionCardComponent } from './competition-card/competition-card.component';

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    SpinachItemsComponent,
    CompetitionComponent,
    CompetitionPlayerComponent,
    CompetitionDetailComponent,
    NavigationBarComponent,
    CompetitionCardComponent,
  ],
  imports: [
    BrowserModule,
    NgZorroAntdModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    AppRoutingModule
  ],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [AppComponent]
})
export class AppModule { }
