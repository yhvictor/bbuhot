import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { GameAccessor } from '../game-list-service';

@Component({
  selector: 'bbuhot-bet-game-entry',
  templateUrl: './bet-game-entry.component.html',
  styleUrls: ['./bet-game-entry.component.scss']
})
export class BetGameEntryComponent implements OnInit {
  @Input() game: GameAccessor;
  @Output() bet = new EventEmitter();

  constructor() {}

  ngOnInit() {}

  onClickBet(): void {
    this.bet.emit();
  }
}
