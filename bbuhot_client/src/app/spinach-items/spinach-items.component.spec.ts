import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpinachItemsComponent } from './spinach-items.component';

describe('SpinachItemsComponent', () => {
  let component: SpinachItemsComponent;
  let fixture: ComponentFixture<SpinachItemsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SpinachItemsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpinachItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
