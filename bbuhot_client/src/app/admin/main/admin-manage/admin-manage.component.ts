import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../../api/api-service';
import { Game, ListGameRequest } from '../../../proto/bbuhot/service/game_pb';
import { AdminManageItem } from './models/admin-manage-item.model';

@Component({
  selector: 'bbuhot-admin-manage',
  templateUrl: './admin-manage.component.html',
  styleUrls: ['./admin-manage.component.css']
})
export class AdminManageComponent implements OnInit {
  itemsArr: Array<AdminManageItem>;
  gamesList: Game[];

  constructor(private apiService: ApiService) {
    this.setupItems();
    this.loadGamesListData();
  }

  loadGamesListData() {
    const listGameRequest = new ListGameRequest();
    listGameRequest.setIsAdminRequest(true);
    listGameRequest.setGameStatus(Game.Status.DRAFT);

    this.apiService.listGames(listGameRequest).subscribe(
      (reply) => {
        this.gamesList = reply.getGamesList();
        console.log(this.gamesList);
      },
      (error) => {
        console.log(error);
      }
    );
  }

  setupItems() {
    const item1 = new AdminManageItem(
      '1',
      'DOTA2',
      'TI8总决赛',
      'LGD',
      'OG',
      '进行中',
      '2018-10-31 23:12:00',
      '操作待定'
    );
    const item2 = new AdminManageItem(
      '2',
      '白头',
      '哥谭皇位赛',
      '朴皇',
      '熊猫头',
      '进行中',
      '2018-10-31 23:12:00',
      '打爆头'
    );
    this.itemsArr = new Array<AdminManageItem>();
    this.itemsArr.push(item1);
    this.itemsArr.push(item2);
  }

  ngOnInit() {}
}
