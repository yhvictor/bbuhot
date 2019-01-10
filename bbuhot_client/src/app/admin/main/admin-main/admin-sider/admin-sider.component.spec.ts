import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSiderComponent } from './admin-sider.component';

describe('AdminSiderComponent', () => {
  let component: AdminSiderComponent;
  let fixture: ComponentFixture<AdminSiderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminSiderComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSiderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
