import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { DataSourceService } from './data-source.service';
import { DataStoreService } from './data-store.service';
import { HttpErrorInterceptor } from './http-error.interceptor';

@NgModule({
  declarations: [],
  imports: [CommonModule, HttpClientModule],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true },
    DataSourceService,
    DataStoreService
  ]
})
export class DataStoreModule {}
