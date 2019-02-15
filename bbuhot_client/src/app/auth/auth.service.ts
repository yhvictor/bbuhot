import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { DataSourceService } from '../data-store/data-source.service';
import { DataStoreService } from '../data-store/data-store.service';
import { AuthReply, AuthRequest } from '../proto/bbuhot/service/auth_pb';

@Injectable()
export class AuthService {
  user: AuthReply.User;
  isLoggedIn = false;

  constructor(private source: DataSourceService, private store: DataStoreService) {}

  public userLogin(auth: string, saltKey: string): Observable<AuthReply.User> {
    const req = new AuthRequest();
    req.setAuth(auth);
    req.setSaltKey(saltKey);

    return this.source.userLogin(req).pipe(
      map((reply: AuthReply) => {
        switch (reply.getErrorCode()) {
          case AuthReply.AuthErrorCode.NO_ERROR:
            this.store.auth = req;
            this.user = reply.getUser();
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
