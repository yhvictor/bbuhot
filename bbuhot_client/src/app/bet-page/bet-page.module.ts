import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { BetGameEntryComponent } from './bet-game-entry/bet-game-entry.component';
import { BetPageComponent } from './bet-page/bet-page.component';
import { GameDetailPageComponent } from './game-detail-page/game-detail-page.component';
import { GameListService } from './game-list-service';
import { UserInformationComponent } from './user-information/user-information.component';

@NgModule({
  declarations: [
    BetPageComponent,
    BetGameEntryComponent,
    GameDetailPageComponent,
    UserInformationComponent
  ],
  imports: [CommonModule],
  exports: [BetPageComponent, GameDetailPageComponent],
  providers: [GameListService]
})
export class BetPageModule {}
