import { Component, Input, OnInit } from '@angular/core';
import { SiderModel } from '../models/sider.model';

@Component({
  selector: 'bbuhot-admin-sider',
  templateUrl: './admin-sider.component.html',
  styleUrls: ['./admin-sider.component.css']
})
export class AdminSiderComponent implements OnInit {
  @Input() siderArr: Array<SiderModel>;
  constructor() {}

  ngOnInit() {}
}
