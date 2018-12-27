import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import zh from '@angular/common/locales/zh';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { zh_CN, NgZorroAntdModule, NZ_I18N } from 'ng-zorro-antd';
import { AppComponent } from './app.component';
import { HomepageComponent } from './homepage/homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';

import { AppRoutingModule } from './app-routing.module';
import { ApiService } from './bbuhot-api/api-service';
import { CompetitionDetailComponent } from './competition-detail/competition-detail.component';
import { CompetitionPlayerComponent } from './competition-player/competition-player.component';
import { CompetitionComponent } from './competition/competition.component';
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { WayneComponent } from './wayne/wayne.component';
import { BbuhotCardComponent } from './bbuhot-card/bbuhot-card.component';

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
    LeaderBoardComponent,
    WayneComponent
    BbuhotCardComponent,
  ],
  imports: [BrowserModule, NgZorroAntdModule, FormsModule, HttpClientModule, BrowserAnimationsModule, AppRoutingModule],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }, { provide: LocationStrategy, useClass: HashLocationStrategy }, ApiService],
  bootstrap: [AppComponent]
})
export class AppModule {}
