import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ApiMethods } from './api-methods';
import { ApiService } from './api-service';
import { AuthGuard, UserInformationHelper } from './user-info-helper';

@NgModule({
  imports: [HttpClientModule],
  providers: [ApiService, ApiMethods, UserInformationHelper, AuthGuard]
})
export class ApiModule {}
