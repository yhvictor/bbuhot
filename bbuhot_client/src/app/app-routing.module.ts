import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './auth/auth.guard';
import { LeaderboardComponent } from './facade/leaderboard/leaderboard.component';
import { LobbyComponent } from './facade/lobby/lobby.component';
import { LoginComponent } from './facade/login/login.component';
import { SeriesComponent } from './facade/series/series.component';

const routes: Routes = [
  { path: '', redirectTo: '/lobby', pathMatch: 'full' },
  { path: 'lobby', component: LobbyComponent, canActivate: [AuthGuard] },
  { path: 'series', component: SeriesComponent, canActivate: [AuthGuard] },
  { path: 'leaderboard', component: LeaderboardComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'admin', loadChildren: './bookmaker/bookmaker.module#BookmakerModule', canActivate: [AuthGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule {}
