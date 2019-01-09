import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { zh_CN, NZ_I18N } from 'ng-zorro-antd';
import { AdminMainModule } from './admin/main/admin-main/admin-main.module';
import { CompetitionModule } from './competition/competition.module';
import { HomepageModule } from './homepage/homepage.module';
import { LeaderBoardModule } from './leader-board/leader-board.module';
import { RoutingComponent } from './routing/routing.component';
import { RoutingModule } from './routing/routing.module';
import { WayneModule } from './wayne/wayne.module';

registerLocaleData(zh);

@NgModule({
  imports: [
    BrowserModule,
    RoutingModule,
    CompetitionModule,
    HomepageModule,
    LeaderBoardModule,
    WayneModule,
    AdminMainModule
  ],
  providers: [{ provide: NZ_I18N, useValue: zh_CN }],
  bootstrap: [RoutingComponent],
  declarations: []
})
export class AppModule {}
