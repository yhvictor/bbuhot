import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BbuhotCardComponent } from './bbuhot-card.component';

describe('BbuhotCardComponent', () => {
  let component: BbuhotCardComponent;
  let fixture: ComponentFixture<BbuhotCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BbuhotCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BbuhotCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
