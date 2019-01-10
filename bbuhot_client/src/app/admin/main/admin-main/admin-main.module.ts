import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgZorroAntdModule } from 'ng-zorro-antd';
import { AdminMainComponent } from './admin-main.component';
import { AdminSiderComponent } from './admin-sider/admin-sider.component';

@NgModule({
  declarations: [AdminMainComponent, AdminSiderComponent],
  imports: [CommonModule, NgZorroAntdModule]
})
export class AdminMainModule {}
