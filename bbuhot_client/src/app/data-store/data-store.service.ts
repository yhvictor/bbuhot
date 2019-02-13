import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { catchError, filter, map } from 'rxjs/operators';

import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import {
  AdminGameReply,
  AdminGameRequest,
  AdminGameStatusReply,
  AdminGameStatusRequest,
  BetReply,
  BetRequest,
  Game,
  ListGameReply,
  ListGameRequest
} from '../proto/bbuhot/service/game_pb';
import { DataSourceService } from './data-source.service';

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  constructor(private source: DataSourceService) {}

  // public listGames(isAdmin: boolean, gameStatus: Game.Status): Game[] {}

  // public updateGame(game: Game): Game {}

  // public changeStatus(gameId: number, gameStatus: Game.Status, winningOption: number): Game {}

  // public betOnGame(gameId: number, bets: Game.Bet[]): Game.Bet[] {}

  public userLogin(auth: string, saltKey: string): Observable<AuthReply.User> {
    const req = new AuthRequest();
    req.setAuth(auth);
    req.setSaltKey(saltKey);

    return this.source.userLogin(req).pipe(
      map((reply: AuthReply) => {
        switch (reply.getErrorCode()) {
          case AuthReply.AuthErrorCode.NO_ERROR:
            return reply.getUser();
            break;
          case AuthReply.AuthErrorCode.KEY_NOT_MATCHING:
            throw new Error('Key not matching');
          case AuthReply.AuthErrorCode.PERMISSION_DENY:
            throw new Error('Permission denied');
          default:
            throw new Error('Internal error');
        }
      })
    );
  }
}
