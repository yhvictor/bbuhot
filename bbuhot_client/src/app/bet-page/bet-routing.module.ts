import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BetPageComponent } from './bet-page/bet-page.component';
import { GameDetailPageComponent } from './game-detail-page/game-detail-page.component';

const betRoutes: Routes = [
  { path: 'bet', component: BetPageComponent },
  { path: 'bet/:id', component: GameDetailPageComponent }
];

@NgModule({
  imports: [RouterModule.forChild(betRoutes)]
})
export class BetRoutingModule {}
