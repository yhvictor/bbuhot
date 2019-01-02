import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api-service';
import { AuthRequest } from '../proto/bbuhot/service/auth_pb';
import { Game, ListGameRequest } from '../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  gamesList: Game[];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadGamesListData();
  }

  loadGamesListData() {
    const listGameRequest = new ListGameRequest();
    listGameRequest.setAuth(new AuthRequest());
    listGameRequest.getAuth().setUid(1);
    listGameRequest
      .getAuth()
      .setAuth('f864Wjt+ccE9euGuZQppnfu5aeSSuWkuVPt91ou9mcUAtMwHgvTfDoqX0nT2fgOb6ykQ22WzfOPZVxoHwT7I');
    listGameRequest.getAuth().setSaltkey('T9Zz8d5b');
    listGameRequest.setIsAdminRequest(true);
    listGameRequest.setGameStatus(Game.Status.DRAFT);

    this.apiService.listGames(listGameRequest).subscribe(
      (reply) => {
        this.gamesList = reply.getGamesList();
      },
      (error) => {
        console.log(error);
      }
    );
  }

  onClickMe() {}
}
