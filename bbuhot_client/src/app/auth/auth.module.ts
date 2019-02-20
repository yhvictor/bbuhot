import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ApiModule } from '../api/api.module';
import { AuthGuard } from './auth.guard';
import { AuthService } from './auth.service';

@NgModule({
  declarations: [],
  imports: [CommonModule, ApiModule],
  providers: [AuthService, AuthGuard]
})
export class AuthModule {}
