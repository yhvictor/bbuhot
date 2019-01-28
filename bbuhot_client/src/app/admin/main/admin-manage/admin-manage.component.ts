import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../api/api-service';
import { Game, ListGameRequest } from '../../../proto/bbuhot/service/game_pb';

@Component({
  selector: 'bbuhot-admin-manage',
  templateUrl: './admin-manage.component.html',
  styleUrls: ['./admin-manage.component.css']
})
export class AdminManageComponent implements OnInit {
  gamesList: Game[];

  constructor(private apiService: ApiService) {
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

  ngOnInit() {}
}
