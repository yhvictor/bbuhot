import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameAccessor, GameListService } from '../game-list-service';

@Component({
  selector: 'bbuhot-bet-page',
  templateUrl: './bet-page.component.html',
  styleUrls: ['./bet-page.component.scss']
})
export class BetPageComponent implements OnInit {
  games: Array<GameAccessor>;

  constructor(private router: Router, private gameListService: GameListService) {}

  async ngOnInit() {
    this.games = await this.gameListService.getGames();

    console.log(this.games);
  }

  onBet(game: GameAccessor) {
    this.router.navigate(['bet', game.id]);
  }
}
