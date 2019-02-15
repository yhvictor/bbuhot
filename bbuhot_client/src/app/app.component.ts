import { Component, OnInit } from '@angular/core';

import { DataStoreService } from './data-store/data-store.service';

@Component({
  selector: 'bbuhot-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  constructor(private store: DataStoreService) {}

  ngOnInit() {}
}
