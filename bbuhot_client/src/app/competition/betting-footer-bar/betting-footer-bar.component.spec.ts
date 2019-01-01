import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BbuhotFooterBarComponent } from './betting-footer-bar.component';

describe('BbuhotFooterBarComponent', () => {
  let component: BbuhotFooterBarComponent;
  let fixture: ComponentFixture<BbuhotFooterBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BbuhotFooterBarComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BbuhotFooterBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
