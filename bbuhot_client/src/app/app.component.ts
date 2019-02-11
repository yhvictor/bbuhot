import { Component, OnInit } from '@angular/core';

import { DataSourceService } from './data-store/data-source.service';
import { AuthReply, AuthRequest } from './proto/bbuhot/service/auth_pb';

@Component({
  selector: 'bbuhot-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  authReply: AuthReply.AsObject;

  constructor(private dataSourceService: DataSourceService) {}

  ngOnInit() {
    this.login();
  }

  login(): void {
    const authRequest = new AuthRequest();
    authRequest.setAuth('f864Wjt+ccE9euGuZQppnfu5aeSSuWkuVPt91ou9mcUAtMwHgvTfDoqX0nT2fgOb6ykQ22WzfOPZVxoHwT7I');
    authRequest.setSaltKey('T9Zz8d5b');

    this.dataSourceService.userLogin(authRequest).subscribe((authReply) => (this.authReply = authReply.toObject()));
  }
}
