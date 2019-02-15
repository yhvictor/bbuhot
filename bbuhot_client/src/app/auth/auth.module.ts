import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { AuthGuard } from './auth.guard';
import { AuthService } from './auth.service';

@NgModule({
  declarations: [],
  imports: [CommonModule],
  providers: [AuthService, AuthGuard]
})
export class AuthModule {}
