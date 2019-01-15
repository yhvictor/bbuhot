import { Component, Input, OnInit } from '@angular/core';
import { AdminManageItem } from '../models/admin-manage-item.model';

@Component({
  selector: 'bbuhot-admin-manage-table',
  templateUrl: './admin-manage-table.component.html',
  styleUrls: ['./admin-manage-table.component.css']
})
export class AdminManageTableComponent implements OnInit {
  @Input() itemsArr: Array<AdminManageItem>;
  constructor() {}

  ngOnInit() {}
}
