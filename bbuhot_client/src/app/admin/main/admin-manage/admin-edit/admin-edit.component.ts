import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'bbuhot-admin-edit',
  templateUrl: './admin-edit.component.html',
  styleUrls: ['./admin-edit.component.css']
})
export class AdminEditComponent implements OnInit {
  @Input() isVisible: boolean;
  @Output() outputIsVisible: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() {}
  ngOnInit() {}

  handleOk(): void {
    this.isVisible = false;
    this.outputIsVisible.emit(false);
  }

  handleCancel(): void {
    this.isVisible = false;
    this.outputIsVisible.emit(false);
  }
}
