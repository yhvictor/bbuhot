import { Component, Input, OnInit } from '@angular/core';
import { Game } from '../../../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-admin-manage-table',
  templateUrl: './admin-manage-table.component.html',
  styleUrls: ['./admin-manage-table.component.css']
})
export class AdminManageTableComponent implements OnInit {
  @Input() gamesList: Game[];
  constructor() {}

  ngOnInit() {
    this.gamesList = [];
  }
}
