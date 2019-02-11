import { Injectable } from '@angular/core';

import { DataSourceService } from './data-source.service';

@Injectable({
  providedIn: 'root'
})
export class DataStoreService {
  constructor(private source: DataSourceService) {}

  get(resource: string) {}

}
