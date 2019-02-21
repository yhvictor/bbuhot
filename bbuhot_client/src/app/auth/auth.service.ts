import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { DataSourceService } from '../data-store/data-source.service';
import { DataStoreService } from '../data-store/data-store.service';
import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';
import { AuthError } from './auth-error';

@Injectable()
export class AuthService {
  user: AuthReply.User;

  constructor(private source: DataSourceService, private store: DataStoreService) {}

  public userLogin(auth: string, saltKey: string): Observable<AuthReply.User> {
    const req = new AuthRequest();
    req.setAuth(auth);
    req.setSaltKey(saltKey);

    return this.source.userLogin(req).pipe(
      map((reply: AuthReply) => {
        if (reply.getErrorCode() === AuthReply.AuthErrorCode.NO_ERROR) {
          this.user = reply.getUser();
          this.store.auth = req;
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
