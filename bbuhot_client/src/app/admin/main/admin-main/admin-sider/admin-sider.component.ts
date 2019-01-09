import { Component, Input, OnInit } from '@angular/core';
import { SiderModels } from '../models/sider.models';

@Component({
  selector: 'bbuhot-admin-sider',
  templateUrl: './admin-sider.component.html',
  styleUrls: ['./admin-sider.component.css']
})
export class AdminSiderComponent implements OnInit {
  @Input() siderArr: Array<SiderModels>;
  constructor() {}

  ngOnInit() {}
}
