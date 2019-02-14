import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

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

interface HasAuthErrorCode {
  getAuthErrorCode(): AuthReply.AuthErrorCode;
}

@Injectable()
export class DataStoreService {
  auth: AuthRequest;
  isLoggedIn = false;

  constructor(private source: DataSourceService) {}

  private handleAuthError<T extends HasAuthErrorCode>(reply: T): void {
    switch (reply.getAuthErrorCode()) {
      case AuthReply.AuthErrorCode.NO_ERROR:
        return;
      case AuthReply.AuthErrorCode.KEY_NOT_MATCHING:
        throw new Error('AuthenticationError: Key Not Matching');
      case AuthReply.AuthErrorCode.PERMISSION_DENY:
        throw new Error('AuthenticationError: Permission Denied');
      default:
        throw new Error('Internal Error');
    }
  }

  private handleStatusError(reply: AdminGameStatusReply): void {
    switch (reply.getGameStatusErrorCode()) {
      case AdminGameStatusReply.ErrorCode.NO_ERROR:
        return;
      case AdminGameStatusReply.ErrorCode.LOCKED:
        throw new Error('StatusError: Game Locked');
      default:
        throw new Error('Internal Error');
    }
  }

  private handleBetError(reply: BetReply): void {
    switch (reply.getBetErrorCode()) {
      case BetReply.BetErrorCode.NO_ERROR:
        return;
      case BetReply.BetErrorCode.MONEY_TOO_LOW:
        throw new Error('BetError: Money Too Low');
      case BetReply.BetErrorCode.MONEY_TOO_HIGH:
        throw new Error('BetError: Money Too High');
      case BetReply.BetErrorCode.OPTION_TOO_MANY:
        throw new Error('BetError: Option Too Many');
      case BetReply.BetErrorCode.NO_ENOUGH_MONEY:
        throw new Error('BetError: No Enough Money');
      default:
        throw new Error('Internal Error');
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

    return this.source.updateGame(req).pipe(
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
        this.handleStatusError(reply);
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
        this.handleBetError(reply);
        return reply.getBetsList();
      })
    );
  }

  // TODO(luciusgone): move login logic to auth service when this service grows too big
  public userLogin(auth: string, saltKey: string): Observable<AuthReply.User> {
    const req = new AuthRequest();
    req.setAuth(auth);
    req.setSaltKey(saltKey);

    return this.source.userLogin(req).pipe(
      map((reply: AuthReply) => {
        switch (reply.getErrorCode()) {
          case AuthReply.AuthErrorCode.NO_ERROR:
            this.auth = req;
            this.isLoggedIn = true;
            return reply.getUser();
          case AuthReply.AuthErrorCode.KEY_NOT_MATCHING:
            throw new Error('AuthenticationError: Key Not Matching');
          case AuthReply.AuthErrorCode.PERMISSION_DENY:
            throw new Error('AuthenticationError: Permission Denied');
          default:
            throw new Error('Internal Error');
        }
      })
    );
  }
}
