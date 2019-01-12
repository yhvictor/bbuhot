import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManagerComponent } from './admin-manager.component';

describe('AdminManagerComponent', () => {
  let component: AdminManagerComponent;
  let fixture: ComponentFixture<AdminManagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminManagerComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
