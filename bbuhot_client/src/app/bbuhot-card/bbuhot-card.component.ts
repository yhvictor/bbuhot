import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Quiz, TeamRatio } from 'src/models';

const STATUS_DOING = 1;
const STATUS_TODO = 2;
const STATUS_DONE = 3;

@Component({ // 装饰器： 赋予一个类更丰富的信息
  selector: 'bbuhot-card', // 匹配的标签名
  templateUrl: './bbuhot-card.component.html', // 组件的模板位置
  styleUrls: ['./bbuhot-card.component.css']
})
// 属性绑定-"[value]"
// <input [value]="myData" />
// 事件绑定-"(keyup)"
// <input (keyup)="handle($event)" />
// 双向绑定-"[(ngModel)]"
// <input [(ngModel)]="myData" />
export class BbuhotCardComponent implements OnInit {

  @Input() quiz: Quiz;
  @Input() teamLeft: TeamRatio;
  @Input() teamRight: TeamRatio;

  @Output() onClick = new EventEmitter<{ $event: Event, team: TeamRatio, quiz: Quiz }>();

  selectedTeam: TeamRatio;

  getStatusStyle() {
    return {
      ['card-status']: true,
      ['card-status-doing']: this.quiz.status === STATUS_DOING,
      ['card-status-todo']: this.quiz.status === STATUS_TODO,
      ['card-status-done']: this.quiz.status === STATUS_DONE,
    };
  }

  getRatioLabelStyle(position: 'left' | 'right') {
    return {
      ['team-ratio']: true,
      [`team-ratio-${position}${this.quiz.status === STATUS_DONE ? '-done' : ''}`]: true,
    };
  }

  onItemClick($event: Event, position: 'left' | 'right') {
    if(this.quiz.status !== STATUS_DONE) {
      this.onClick.emit({
        $event,
        team: this[`team${position.charAt(0).toUpperCase() + position.slice(1)}`],
        quiz: this.quiz,
      });
    } else {
      return;
    }
  }

  isWin(position: 'left' | 'right') {
    return this.quiz.result === position && this.quiz.status === STATUS_DONE;
  }

  // 构造器初始化
  constructor() {

  }
  // 第一次出发数据变化钩子
  onChanges() {

  }
  // 组件初始化
  ngOnInit() {
  }

  // 运行期间出发数据变化钩子
  // onChanges() {

  // }
  // 组件销毁前触发
  onDestroy() {

  }
}