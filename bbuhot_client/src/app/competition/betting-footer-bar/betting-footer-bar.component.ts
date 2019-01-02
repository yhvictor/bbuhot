import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Quiz } from '../models/quiz';
import { TeamRatio } from '../models/team-ratio';
@Component({
  selector: 'bbuhot-betting-footer-bar',
  templateUrl: './betting-footer-bar.component.html',
  styleUrls: ['./betting-footer-bar.component.css']
})
export class BettingFooterBarComponent {
  @Input() quiz: Quiz;
  @Input() selectedTeam: TeamRatio;
  inputValue: string;
  @Input() btnDisabled: Boolean = !this.inputValue;

  @Output() confirmEvent = new EventEmitter<string>();

  constructor() { }

  onBtnClick() {
    if (this.inputValue) {
      this.confirmEvent.emit(this.inputValue);
    } else {
      return;
    }
  }

  onInputChange($event: Event) {
    this.btnDisabled = !this.inputValue;
  }
}
