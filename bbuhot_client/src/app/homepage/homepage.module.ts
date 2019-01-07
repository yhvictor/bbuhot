import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { ApiModule } from '../api/api.module';
import { HomepageComponent } from './homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';

@NgModule({
  declarations: [HomepageComponent, SpinachItemsComponent],
  imports: [HttpClientModule, ApiModule, CommonModule, BrowserModule]
})
export class HomepageModule {}
