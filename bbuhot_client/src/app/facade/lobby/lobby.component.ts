import { Component, OnInit } from '@angular/core';

import { AuthError } from '../../auth/auth-error';
import { BetError } from '../../data-store/data-store-errors';
import { DataStoreService } from '../../data-store/data-store.service';
import { Game } from '../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {
  errorMsg: string;
  upcomingGames: Game[];
  bets: Game.Bet[];

  constructor(private store: DataStoreService) {}

  ngOnInit() {
    this.loadPublishedGames();
  }

  loadPublishedGames(): void {
    this.store.listGames(/* isAdmin= */ false, Game.Status.PUBLISHED).subscribe({
      next: (games: Game[]) => (this.upcomingGames = games),
      error: (err): void => {
        if (err instanceof AuthError) {
          this.errorMsg = `ErrorCode: ${err.errorCode}\nErrorMessage${err.message}`;
        }
      }
    });
  }

  betOnGame(): void {
    const bet = new Game.Bet();
    bet.setBettingOptionId(0);
    bet.setMoney(200);
    this.store.betOnGame(this.upcomingGames[0].getId(), [bet]).subscribe({
      next: (bets: Game.Bet[]) => (this.bets = bets),
      error: (err: BetError): void => {
        if (err instanceof BetError) {
          this.errorMsg = `ErrorCode: ${err.errorCode}\nErrorMessage${err.message}`;
        }
      }
    });
  }
}
