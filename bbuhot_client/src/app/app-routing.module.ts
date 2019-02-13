import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LobbyComponent } from './facade/lobby/lobby.component';
import { LeaderboardComponent } from './facade/leaderboard/leaderboard.component';
import { SeriesComponent } from './facade/series/series.component';

const routes: Routes = [
  { path: '', redirectTo: '/lobby', pathMatch: 'full' },
  { path: 'lobby', component: LobbyComponent },
  { path: 'series', component: SeriesComponent },
  { path: 'leaderboard', component: LeaderboardComponent },
  { path: 'admin', loadChildren: './bookmaker/bookmaker.module#BookmakerModule' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
