import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgZorroAntdModule } from 'ng-zorro-antd';
import { BettingCardComponent } from './betting-card/betting-card.component';
import { BbuhotFooterBarComponent } from './betting-footer-bar/betting-footer-bar.component';
import { CompetitionDetailComponent } from './competition-detail/competition-detail.component';
import { CompetitionPlayerComponent } from './competition-player/competition-player.component';
import { CompetitionComponent } from './competition.component';

@NgModule({
  declarations: [
    CompetitionComponent,
    CompetitionPlayerComponent,
    CompetitionDetailComponent,
    BettingCardComponent,
    BbuhotFooterBarComponent
  ],
  imports: [FormsModule, CommonModule, NgZorroAntdModule]
})
export class CompetitionModule {}
