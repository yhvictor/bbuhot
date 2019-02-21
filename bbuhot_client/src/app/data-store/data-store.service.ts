import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { AuthError } from '../auth/auth-error';
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
import { BetError, GameStatusError } from './data-store-errors';

interface HasAuthErrorCode {
  getAuthErrorCode(): AuthReply.AuthErrorCode;
}

@Injectable()
export class DataStoreService {
  auth: AuthRequest;

  constructor(private source: DataSourceService) {}

  private handleAuthError<T extends HasAuthErrorCode>(reply: T): void {
    if (reply.getAuthErrorCode() === AuthReply.AuthErrorCode.NO_ERROR) {
      return;
    } else {
      throw new AuthError(reply.getAuthErrorCode());
    }
  }

  public listGames(isAdmin: boolean, gameStatus: Game.Status): Observable<Game[]> {
    const req = new ListGameRequest();
    req.setAuth(this.auth);
    req.setIsAdminRequest(isAdmin);
    req.setGameStatus(gameStatus);

    return this.source.listGames(req).pipe(
      map((reply: ListGameReply) => {
        this.handleAuthError(reply);
        return reply.getGamesList();
      })
    );
  }

  public createOrUpdateGame(game: Game): Observable<Game> {
    const req = new AdminGameRequest();
    req.setAuth(this.auth);
    req.setGame(game);

    return this.source.createOrUpdateGame(req).pipe(
      map((reply: AdminGameReply) => {
        this.handleAuthError(reply);
        return reply.getGame();
      })
    );
  }

  public changeStatus(gameId: number, gameStatus: Game.Status, winningOption: number): Observable<Game> {
    const req = new AdminGameStatusRequest();
    req.setAuth(this.auth);
    req.setGameId(gameId);
    req.setGameStatus(gameStatus);
    req.setWinningOption(winningOption);

    return this.source.changeStatus(req).pipe(
      map((reply: AdminGameStatusReply) => {
        this.handleAuthError(reply);
        if (reply.getGameStatusErrorCode() !== AdminGameStatusReply.ErrorCode.NO_ERROR) {
          throw new GameStatusError(reply.getGameStatusErrorCode());
        }
        return reply.getGame();
      })
    );
  }

  public betOnGame(gameId: number, bets: Game.Bet[]): Observable<Game.Bet[]> {
    const req = new BetRequest();
    req.setAuth(this.auth);
    req.setGameId(gameId);
    req.setBetsList(bets);

    return this.source.betOnGame(req).pipe(
      map((reply: BetReply) => {
        this.handleAuthError(reply);
        if (reply.getBetErrorCode() !== BetReply.BetErrorCode.NO_ERROR) {
          throw new BetError(reply.getBetErrorCode());
        }
        return reply.getBetsList();
      })
    );
  }
}
