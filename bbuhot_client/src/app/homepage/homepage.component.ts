import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api-service';
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
