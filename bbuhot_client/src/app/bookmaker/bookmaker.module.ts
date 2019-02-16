import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { WarehouseModule } from '../warehouse/warehouse.module';
import { BookmakerRoutingModule } from './bookmaker-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [DashboardComponent],
  imports: [CommonModule, WarehouseModule, BookmakerRoutingModule]
})
export class BookmakerModule {}
