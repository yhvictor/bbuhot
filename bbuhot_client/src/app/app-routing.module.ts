import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { BetRoutingModule } from './bet-page/bet-routing.module';

const routes: Routes = [{ path: '', redirectTo: '/bet', pathMatch: 'full' }];

@NgModule({
  imports: [RouterModule.forRoot(routes), BetRoutingModule],
  exports: [RouterModule],
  providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }]
})
export class AppRoutingModule {}
