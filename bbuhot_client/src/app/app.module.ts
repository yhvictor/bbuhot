import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { zh_CN, NgZorroAntdModule, NZ_I18N } from 'ng-zorro-antd';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';

import { ApiModule } from './api/api.module';
import { AppRoutingModule } from './app-routing.module';
import { CompetitionModule } from './competition/competition.module';
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { WayneComponent } from './wayne/wayne.component';
import { BbuhotCardComponent } from './bbuhot-card/bbuhot-card.component';
import { BbuhotFooterBarComponent } from './bbuhot-footer-bar/bbuhot-footer-bar.component';

registerLocaleData(zh);

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    SpinachItemsComponent,
    NavigationBarComponent,
    LeaderBoardComponent,
    WayneComponent,
    BbuhotCardComponent,
    BbuhotFooterBarComponent,
  ],
  imports: [
    ApiModule,
    BrowserModule,
    NgZorroAntdModule,
    FormsModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    CompetitionModule
  ],
  providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }, { provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [AppComponent]
})
export class AppModule {}
