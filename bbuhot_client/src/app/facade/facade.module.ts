import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { LobbyComponent } from './lobby/lobby.component';
import { LoginComponent } from './login/login.component';
import { SeriesComponent } from './series/series.component';

@NgModule({
  declarations: [LeaderboardComponent, LobbyComponent, LoginComponent, SeriesComponent],
  imports: [CommonModule],
  exports: [LeaderboardComponent, LobbyComponent, LoginComponent, SeriesComponent]
})
export class FacadeModule {}
