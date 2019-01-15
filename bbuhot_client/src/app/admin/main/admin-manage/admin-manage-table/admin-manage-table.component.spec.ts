import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManageTableComponent } from './admin-manage-table.component';

describe('AdminManageTableComponent', () => {
  let component: AdminManageTableComponent;
  let fixture: ComponentFixture<AdminManageTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminManageTableComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminManageTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
