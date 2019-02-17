import { Component, OnInit, Input } from '@angular/core';

import { Game } from '../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-game-card',
  templateUrl: './game-card.component.html',
  styleUrls: ['./game-card.component.scss']
})
export class GameCardComponent implements OnInit {
  @Input() game: Game;

  constructor() {}

  ngOnInit() {}
}
