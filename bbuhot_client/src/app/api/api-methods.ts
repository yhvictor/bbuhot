import { Injectable } from '@angular/core';
import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import { Game, ListGameRequest } from '../proto/bbuhot/service/game_pb';
import { ApiService } from './api-service';

export class AuthError extends Error {
  constructor(private errorCode: AuthReply.AuthErrorCode) {
    super(AuthError.getErrorMessage(errorCode));
    Object.setPrototypeOf(this, AuthError.prototype);
  }

  private static getErrorMessage(errorCode: AuthReply.AuthErrorCode): string {
    return 'AuthError: ' + errorCode;
  }
}

@Injectable()
export class ApiMethods {
  constructor(private apiService: ApiService) {}

  public async fetchAvailableGames(): Promise<Array<Game>> {
    const listGameRequest = new ListGameRequest();
    listGameRequest.setGameStatus(Game.Status.PUBLISHED);
    listGameRequest.setIsAdminRequest(false);

    const listGameReply = await this.apiService.listGames(listGameRequest).toPromise();
    this.checkAuthError(listGameReply.getAuthErrorCode() as AuthReply.AuthErrorCode);
    return listGameReply.getGamesList();
  }

  public async fetchUserInformation(): Promise<AuthReply.User> {
    const authRequest = new AuthRequest();
    const authReply = await this.apiService.auth(authRequest).toPromise();
    this.checkAuthError(authReply.getErrorCode());
    return authReply.getUser() as AuthReply.User;
  }

  private checkAuthError(errorCode: AuthReply.AuthErrorCode | undefined): void {
    if (errorCode !== undefined && errorCode !== AuthReply.AuthErrorCode.NO_ERROR) {
      throw new AuthError(errorCode);
    }
  }
}
