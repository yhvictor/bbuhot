import { Component, OnInit } from '@angular/core';
import { Routes } from '@angular/router';
import { AdminHistoryComponent } from '../admin-history/admin-history.component';
import { AdminManagerComponent } from '../admin-manager/admin-manager.component';
import { SiderModels } from './models/sider.models';

export const routes: Routes = [
  { path: '', redirectTo: 'manager', pathMatch: 'full' },
  { path: 'manager', component: AdminManagerComponent },
  { path: 'history', component: AdminHistoryComponent }
];

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
    const sider1 = new SiderModels('竞猜管理', 'diff', 'manager');
    const sider2 = new SiderModels('历史竞猜', 'calendar', 'history');
    this.siderArr = new Array<SiderModels>();
    this.siderArr.push(sider1);
    this.siderArr.push(sider2);
  }

  ngOnInit() {}
}
