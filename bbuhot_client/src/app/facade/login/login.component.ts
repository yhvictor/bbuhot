import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { DataStoreService } from '../../data-store/data-store.service';
import { AuthReply } from '../../proto/bbuhot/service/auth_pb';

@Component({
  selector: 'bbuhot-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  errorMsg: string;

  constructor(private store: DataStoreService, private router: Router) {}

  ngOnInit() {
    this.login();
  }

  // TODO(luciusgone): implement real login
  login(): void {
    this.store
      .userLogin(
        /* auth= */ 'f864Wjt+ccE9euGuZQppnfu5aeSSuWkuVPt91ou9mcUAtMwHgvTfDoqX0nT2fgOb6ykQ22WzfOPZVxoHwT7I',
        /* saltKey= */ 'T9Zz8d5b'
      )
      .subscribe({
        next: (user: AuthReply.User) => this.router.navigate(['lobby']),
        error: (err) => (this.errorMsg = err)
      });
  }
}