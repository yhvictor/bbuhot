import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Quiz } from '../models/quiz';
import { TeamRatio } from '../models/team-ratio';

enum CardPosition {
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
  @Input() teams: {
    left: TeamRatio;
    right: TeamRatio;
  };

  @Output() clickEvent = new EventEmitter<{ $event: Event; team: TeamRatio; quiz: Quiz }>();

  selectedTeam: TeamRatio;

  getStatusStyle(): any {
    return {
      ['card-status']: true,
      ['card-status-doing']: this.quiz.status === Quiz.Status.STATUS_DOING,
      ['card-status-todo']: this.quiz.status === Quiz.Status.STATUS_TODO,
      ['card-status-done']: this.quiz.status === Quiz.Status.STATUS_DONE
    };
  }

  getRatioLabelStyle(position: CardPosition): any {
    return {
      ['team-ratio']: true,
      [`team-ratio-${position}${this.quiz.status === Quiz.Status.STATUS_DONE ? '-done' : ''}`]: true
    };
  }

  getRenderIndicator(position: CardPosition): any {
    return {
      ['team-item']: true,
      [`team-${position}`]: true,
      ['team-active_indicator']: this.selectedTeam && this.selectedTeam.teamId === this.teams[position].teamId
    };
  }

  onItemClick($event: Event, position: CardPosition): void {
    if (this.quiz.status !== Quiz.Status.STATUS_DOING) {
      return;
    }
    const newTeam: TeamRatio = this.teams[position];
    if (this.selectedTeam && this.selectedTeam.teamId === newTeam.teamId) {
      // User is clicking on the same team again, just unselect the team.
      this.selectedTeam = undefined;
    } else {
      this.selectedTeam = newTeam;
    }
    this.clickEvent.emit({
      $event,
      team: this.selectedTeam,
      quiz: this.quiz
    });
  }

  isWin(position: CardPosition): boolean {
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
