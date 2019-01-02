import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { zh_CN, NZ_I18N } from 'ng-zorro-antd';
import { CompetitionModule } from './competition/competition.module';
<<<<<<< HEAD
import { HomepageModule } from './homepage/homepage.module';
import { LeaderBoardModule } from './leader-board/leader-board.module';
import { RoutingComponent } from './routing/routing.component';
import { RoutingModule } from './routing/routing.module';
import { WayneModule } from './wayne/wayne.module';
=======
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { WayneComponent } from './wayne/wayne.component';
import { BbuhotCardComponent } from './bbuhot-card/bbuhot-card.component';
import { BbuhotFooterBarComponent } from './bbuhot-footer-bar/bbuhot-footer-bar.component';
>>>>>>> f21683f895f03a00dc4f4dd4e2834ee72c56f882

registerLocaleData(zh);

@NgModule({
<<<<<<< HEAD
  imports: [BrowserModule, RoutingModule, CompetitionModule, HomepageModule, LeaderBoardModule, WayneModule],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [RoutingComponent]
=======
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
>>>>>>> f21683f895f03a00dc4f4dd4e2834ee72c56f882
})
export class AppModule {}
