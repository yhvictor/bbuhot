import { Component, OnInit } from '@angular/core';
import { SiderModels } from './models/sider.models';

@Component({
  selector: 'bbuhot-admin-main',
  templateUrl: './admin-main.component.html',
  styleUrls: ['./admin-main.component.css']
})
export class AdminMainComponent implements OnInit {
  siderArr: Array<SiderModels>;
  constructor() {
    this.setupSider();
  }

  setupSider() {
    const sider1 = new SiderModels('竞猜管理', 'diff');
    const sider2 = new SiderModels('干他妈的熊猫头', 'calendar');
    this.siderArr = new Array<SiderModels>();
    this.siderArr.push(sider1);
    this.siderArr.push(sider2);
  }

  ngOnInit() {}
}
