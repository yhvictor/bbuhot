import { Component, OnInit } from '@angular/core';

import { DataStoreService } from '../../data-store/data-store.service';
import { Game } from '../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {
  games: Game[];

  constructor(private store: DataStoreService) {}

  ngOnInit() {}

  loadGames(): void {
    this.store.listGames(/* isAdmin= */ false, Game.Status.PUBLISHED).subscribe({
      next: (games: Game[]) => (this.games = games),
      error: (err) => console.log(err)
    });
  }
}
