import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompetitionComponent } from './competition/competition.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { WayneComponent } from './wayne/wayne.component';

const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomepageComponent },
  { path: 'competition', component: CompetitionComponent },
  { path: 'leaderboard', component: LeaderBoardComponent },
  { path: 'wayne', component: WayneComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
