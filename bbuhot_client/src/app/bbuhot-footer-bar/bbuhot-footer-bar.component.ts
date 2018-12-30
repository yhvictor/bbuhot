import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Quiz, TeamRatio } from 'src/models';
const STATUS_DOING = 1;
const STATUS_TODO = 2;
const STATUS_DONE = 3;
@Component({
  selector: 'bbuhot-footer-bar',
  templateUrl: './bbuhot-footer-bar.component.html',
  styleUrls: ['./bbuhot-footer-bar.component.css']
})
export class BbuhotFooterBarComponent implements OnInit {

  constructor() {}

  @Input() quiz: Quiz;
  @Input() selectedTeam: TeamRatio;
  @Output() onConfirm = new EventEmitter<string>();
  inputValue: string;
  @Input() btnDisabled: Boolean = !this.inputValue;
  ngOnInit() {
  }

  btnClick() {
    if(this.inputValue) {
      this.onConfirm.emit(this.inputValue);
    } else {
      return;
    }
  }
  onInputChange($event: Event) {
    this.btnDisabled = !this.inputValue;
  }
}
