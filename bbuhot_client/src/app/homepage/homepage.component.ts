import { Component, OnInit } from '@angular/core';
import { ApiService } from '../bbuhot-api/api-service';
import { Game, ListGameRequest } from '../proto/bbuhot/service/game_pb';
import { AuthRequest } from '../proto/bbuhot/service/auth_pb';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  constructor(private apiService:ApiService) { }

  ngOnInit() {
  }

  onClickMe() {
    const listGameRequest = new ListGameRequest();
    listGameRequest.setAuth(new AuthRequest());
    listGameRequest.getAuth().setUid(1);
    listGameRequest.getAuth().setAuth("986atk1/mbgC3AeOxeKBREp4eE8WbUpfDtKrliINd7DzV8nrAp1ZpM9KEJYT935EF5EwFlAidT7/4H19q3tc");
    listGameRequest.getAuth().setSaltkey("RaIEjNbx");
    listGameRequest.setIsAdminRequest(true);
    listGameRequest.setGameStatus(Game.Status.DRAFT);

    this.apiService.listGames(listGameRequest).subscribe((reply) => {
      console.log(reply.toObject());
    }, (error) => {
      console.log(error);
    });
  }
}
