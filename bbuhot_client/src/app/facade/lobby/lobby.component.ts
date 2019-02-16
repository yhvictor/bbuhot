import { Component, OnInit } from '@angular/core';

import { DataStoreService } from '../../data-store/data-store.service';
import { Game } from '../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {
  upcomingGames: Game[];

  constructor(private store: DataStoreService) {}

  ngOnInit() {
    this.loadPublishedGames();
  }

  loadPublishedGames(): void {
    this.store.listGames(/* isAdmin= */ false, Game.Status.PUBLISHED).subscribe({
      next: (games: Game[]) => (this.upcomingGames = games),
      error: (err) => console.log(err)
    });
  }
}
