import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { LobbyComponent } from './lobby/lobby.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { SeriesComponent } from './series/series.component';

@NgModule({
  declarations: [LobbyComponent, LeaderboardComponent, SeriesComponent],
  imports: [CommonModule],
  exports: [LobbyComponent, LeaderboardComponent, SeriesComponent]
})
export class FacadeModule {}
