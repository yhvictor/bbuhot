import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthReply } from '../proto/bbuhot/service/auth_pb';
import { ApiMethods } from './api-methods';

@Injectable()
export class UserInformationHelper {
  user: AuthReply.User;

  constructor(private apiMethods: ApiMethods) {}

  public async getUserInformationOrFetch(): Promise<AuthReply.User> {
    return this.user ? this.user : (this.user = await this.apiMethods.fetchUserInformation());
  }
}

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private userInformationHelper: UserInformationHelper, private router: Router) {}

  async canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    try {
      await this.userInformationHelper.getUserInformationOrFetch();
      return true;
    } catch (e) {
      // TODO(yh_victor): handle login
      // this.router.navigate(['login']);
      console.log(e);
      console.log('Please login');
      return false;
    }
  }
}
