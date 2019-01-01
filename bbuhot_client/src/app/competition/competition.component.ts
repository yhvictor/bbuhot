import { Component, OnInit } from '@angular/core';
import { Quiz, TeamRatio } from 'src/models';

@Component({
  selector: 'app-competition',
  templateUrl: './competition.component.html',
  styleUrls: ['./competition.component.css']
})
export class CompetitionComponent implements OnInit {
  QuizArr: Array<Quiz> = [];

  QuizArr: Array<Quiz> = [];

  constructor() {
    this.QuizArr = [
      new Quiz(1, '猜输赢', 2, '未开始', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      new Quiz(2, '第一局赢', 2, '未开始', new TeamRatio(1, 'PSG.LGD', 0.14), new TeamRatio(2, 'OasdG', 0.10)),
      new Quiz(3, '猜输赢', 3, '已结束', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18), 'left'),
      new Quiz(4, '猜输赢', 2, '未开始', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      // new Quiz(5, '猜输赢', 1, '进行中', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18)),
      // new Quiz(6, '第二局赢', 3, '已结束', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18), 'right'),
      new Quiz(7, '猜输赢', 1, '进行中', new TeamRatio(1, 'PSG.LGD', 0.12), new TeamRatio(2, 'OG', 0.18))
    ];
  }

  onItemClick({ $event, team, quiz }: { $event: Event, team: TeamRatio, quiz: Quiz }) {
    // TODO
    console.log('TODO: onLeftItemClick', $event, team, quiz);
  }
  onConfirmClick (inputValue: string) {
    // 点击确定按钮将输入框的值带入
    console.log(inputValue);
  }
  ngOnInit() {
  }

}
