import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { WarehouseModule } from '../warehouse/warehouse.module';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { LobbyComponent } from './lobby/lobby.component';
import { LoginComponent } from './login/login.component';
import { SeriesComponent } from './series/series.component';

@NgModule({
  declarations: [LobbyComponent, LeaderboardComponent, SeriesComponent, LoginComponent],
  imports: [CommonModule, WarehouseModule],
  exports: [LobbyComponent, LeaderboardComponent, SeriesComponent, LoginComponent]
})
export class FacadeModule {}
