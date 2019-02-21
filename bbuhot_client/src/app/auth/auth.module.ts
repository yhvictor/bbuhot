import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { DataStoreModule } from '../data-store/data-store.module';
import { AuthGuard } from './auth.guard';
import { AuthService } from './auth.service';

@NgModule({
  declarations: [],
  imports: [CommonModule, DataStoreModule],
  providers: [AuthService, AuthGuard]
})
export class AuthModule {}
