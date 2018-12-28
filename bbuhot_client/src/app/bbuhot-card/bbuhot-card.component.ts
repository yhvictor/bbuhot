import { Component, OnInit } from '@angular/core';

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

  // 构造器初始化
  constructor() { }
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