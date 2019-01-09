import { CommonModule, HashLocationStrategy, LocationStrategy } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NgZorroAntdModule } from 'ng-zorro-antd';
import { AdminMainComponent } from '../admin/main/admin-main/admin-main.component';
import { CompetitionComponent } from '../competition/competition.component';
import { HomepageComponent } from '../homepage/homepage.component';
import { LeaderBoardComponent } from '../leader-board/leader-board.component';
import { WayneComponent } from '../wayne/wayne.component';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { RoutingComponent } from './routing.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomepageComponent },
  { path: 'competition', component: CompetitionComponent },
  { path: 'leaderboard', component: LeaderBoardComponent },
  { path: 'wayne', component: WayneComponent },
  { path: 'admin', component: AdminMainComponent }
];

@NgModule({
  declarations: [RoutingComponent, NavigationBarComponent],
  imports: [RouterModule.forRoot(routes), NgZorroAntdModule, CommonModule],
  providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]
})
export class RoutingModule {}
