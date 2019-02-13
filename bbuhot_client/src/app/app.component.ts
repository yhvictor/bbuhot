import { Component, OnInit } from '@angular/core';

import { DataStoreService } from './data-store/data-store.service';
import { AuthReply } from './proto/bbuhot/service/auth_pb';

@Component({
  selector: 'bbuhot-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  user: AuthReply.User.AsObject;

  constructor(private store: DataStoreService) {}

  ngOnInit() {
    this.login();
  }

  login(): void {
    this.store
      .userLogin(
        /* auth= */ 'f864Wjt+ccE9euGuZQppnfu5aeSSuWkuVPt91ou9mcUAtMwHgvTfDoqX0nT2fgOb6ykQ22WzfOPZVxoHwT7I',
        /* saltKey= */ 'T9Zz8d5b'
      )
      .subscribe((user) => (this.user = user.toObject()));
  }
}
