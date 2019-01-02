import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Quiz } from '../models/quiz';
import { TeamRatio } from '../models/team-ratio';

enum Position {
  LEFT = 'left',
  RIGHT = 'right'
}

@Component({
  selector: 'bbuhot-betting-card',
  templateUrl: './betting-card.component.html',
  styleUrls: ['./betting-card.component.css']
})
export class BettingCardComponent {
  @Input() quiz: Quiz;
  @Input() teamLeft: TeamRatio;
  @Input() teamRight: TeamRatio;

  @Output() clickEvent = new EventEmitter<{ $event: Event; team: TeamRatio; quiz: Quiz }>();

  selectedTeam: TeamRatio;

  constructor() {}

  getStatusStyle(): any {
    return {
      ['card-status']: true,
      ['card-status-doing']: this.quiz.status === Quiz.Status.STATUS_DOING,
      ['card-status-todo']: this.quiz.status === Quiz.Status.STATUS_TODO,
      ['card-status-done']: this.quiz.status === Quiz.Status.STATUS_DONE
    };
  }

  getRatioLabelStyle(position: Position) {
    return {
      ['team-ratio']: true,
      [`team-ratio-${position}${this.quiz.status === Quiz.Status.STATUS_DONE ? '-done' : ''}`]: true
    };
  }

  renderIndicator(position: Position): any {
    return {
      ['team-item']: true,
      [`team-${position}`]: true,
      ['team-active_indicator']:
        this.selectedTeam &&
        this.selectedTeam.teamId === this[`team${position.charAt(0).toUpperCase() + position.slice(1)}`].teamId
    };
  }

  onItemClick($event: Event, position: Position): void {
    if (this.quiz.status !== Quiz.Status.STATUS_DONE) {
      const newTeam: TeamRatio = this[`team${position.charAt(0).toUpperCase() + position.slice(1)}`];
      if(this.selectedTeam && this.selectedTeam.teamId === newTeam.teamId) {
        this.selectedTeam = undefined;
      } else {
        this.selectedTeam = newTeam;
      }
      this.clickEvent.emit({
        $event,
        team: this.selectedTeam,
        quiz: this.quiz
      });
    } else {
      return;
    }
  }

  isWin(position: Position): boolean {
    return this.quiz.result === position && this.quiz.status === Quiz.Status.STATUS_DONE;
  }

  getStatusName(): string {
    switch (this.quiz.status) {
      case Quiz.Status.STATUS_DOING:
        return '进行中';
      case Quiz.Status.STATUS_DONE:
        return '已完成';
      case Quiz.Status.STATUS_TODO:
        return '未开始';
    }
  }
}
