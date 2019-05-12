import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { Game } from 'src/app/proto/bbuhot/service/game_pb';
import { GameAccessor, GameListService } from '../game-list-service';

@Component({
  selector: 'bbuhot-game-detail-page',
  templateUrl: './game-detail-page.component.html',
  styleUrls: ['./game-detail-page.component.scss']
})
export class GameDetailPageComponent implements OnInit {
  public game: GameAccessor | undefined;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private gameListService: GameListService
  ) {}

  async ngOnInit() {
    console.log(this.activatedRoute.params);
    const params = this.activatedRoute.params as BehaviorSubject<Params>;
    const id = params.getValue()['id'];
    console.log(id);
    this.game = await this.gameListService.getGame(Number(id));
    console.log(this.game);
  }

  onClickBack(): void {
    this.router.navigate(['bet']);
  }
}
