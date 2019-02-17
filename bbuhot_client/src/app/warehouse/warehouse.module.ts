import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BannerComponent } from './banner/banner.component';
import { GameCardComponent } from './game-card/game-card.component';
import { GameDetailCardComponent } from './game-detail-card/game-detail-card.component';
import { HeaderComponent } from './header/header.component';
import { ImageHeadingComponent } from './image-heading/image-heading.component';
import { NotificationComponent } from './notification/notification.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { UserInfoCardComponent } from './user-info-card/user-info-card.component';

@NgModule({
  declarations: [
    BannerComponent,
    GameCardComponent,
    GameDetailCardComponent,
    HeaderComponent,
    ImageHeadingComponent,
    NotificationComponent,
    SidebarComponent,
    UserInfoCardComponent,
  ],
  imports: [CommonModule, RouterModule],
  exports: [
    BannerComponent,
    GameCardComponent,
    GameDetailCardComponent,
    HeaderComponent,
    ImageHeadingComponent,
    NotificationComponent,
    SidebarComponent,
    UserInfoCardComponent
  ]
})
export class WarehouseModule {}
