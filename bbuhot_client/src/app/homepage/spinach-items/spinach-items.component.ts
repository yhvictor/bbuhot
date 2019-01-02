import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-spinach-items',
  templateUrl: './spinach-items.component.html',
  styleUrls: ['./spinach-items.component.css']
})
export class SpinachItemsComponent implements OnInit {
  @Input() game: Game;
  constructor() {}

  ngOnInit() {}
}
