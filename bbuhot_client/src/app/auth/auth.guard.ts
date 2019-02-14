import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

import { DataStoreService } from '../data-store/data-store.service';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private store: DataStoreService, private router: Router) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.store.isLoggedIn) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
