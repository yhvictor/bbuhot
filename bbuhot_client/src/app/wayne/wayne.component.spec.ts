import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WayneComponent } from './wayne.component';

describe('WayneComponent', () => {
  let component: WayneComponent;
  let fixture: ComponentFixture<WayneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WayneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WayneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
