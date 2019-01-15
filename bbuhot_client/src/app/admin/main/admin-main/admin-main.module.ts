import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgZorroAntdModule } from 'ng-zorro-antd';
import { AdminHistoryComponent } from '../admin-history/admin-history.component';
import { AdminManageComponent } from '../admin-manage/admin-manage.component';
import { AdminMainComponent } from './admin-main.component';
import { AdminSiderComponent } from './admin-sider/admin-sider.component';

@NgModule({
  declarations: [AdminMainComponent, AdminSiderComponent, AdminManageComponent, AdminHistoryComponent],
  imports: [CommonModule, NgZorroAntdModule, RouterModule]
})
export class AdminMainModule {}
