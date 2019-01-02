import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';
import { ApiModule } from '../api/api.module';
import { HomepageComponent } from './homepage.component';
import { SpinachItemsComponent } from './spinach-items/spinach-items.component';

@NgModule({
  declarations: [HomepageComponent, SpinachItemsComponent],
  imports: [HttpClientModule, ApiModule]
})
export class HomepageModule {}
