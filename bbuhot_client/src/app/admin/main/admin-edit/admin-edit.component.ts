import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'bbuhot-admin-edit',
  templateUrl: './admin-edit.component.html',
  styleUrls: ['./admin-edit.component.css']
})
export class AdminEditComponent implements OnInit {
  @Input() isVisible: boolean;

  constructor() {}
  ngOnInit() {
    this.isVisible = false;
  }

  handleOk(): void {
    this.isVisible = false;
  }

  handleCancel(): void {
    this.isVisible = false;
  }
}
