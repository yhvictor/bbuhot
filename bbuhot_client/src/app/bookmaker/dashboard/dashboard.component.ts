import { Component, OnInit } from '@angular/core';

import { DataStoreService } from '../../data-store/data-store.service';

@Component({
  selector: 'bbuhot-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  constructor(private store: DataStoreService) {}

  ngOnInit() {}
}
