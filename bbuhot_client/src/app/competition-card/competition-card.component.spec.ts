import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetitionCardComponent } from './competition-card.component';

describe('CompetitionCardComponent', () => {
  let component: CompetitionCardComponent;
  let fixture: ComponentFixture<CompetitionCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompetitionCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetitionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
