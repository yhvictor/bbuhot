import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookmakerRoutingModule } from './bookmaker-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [DashboardComponent],
  imports: [
    CommonModule,
    BookmakerRoutingModule
  ]
})
export class BookmakerModule { }
