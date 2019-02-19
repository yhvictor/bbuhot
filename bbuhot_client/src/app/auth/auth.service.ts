import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api-service';
import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import { AuthError } from './auth-error';

@Injectable()
export class AuthService {
  user: AuthReply.User;

  constructor(private source: ApiService) {}

  public userLogin(auth: string, saltKey: string): Observable<AuthReply.User> {
    const req = new AuthRequest();
    req.setAuth(auth);
    req.setSaltKey(saltKey);

    return this.source.userLogin(req).pipe(
      map((reply: AuthReply) => {
        if (reply.getErrorCode() === AuthReply.AuthErrorCode.NO_ERROR) {
          this.user = reply.getUser();
          return reply.getUser();
        } else {
          throw new AuthError(reply.getErrorCode());
        }
      })
    );
  }

  public isLoggedIn(): boolean {
    return this.user ? true : false;
  }
}
