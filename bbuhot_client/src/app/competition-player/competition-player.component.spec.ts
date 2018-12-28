import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompetitionPlayerComponent } from './competition-player.component';

describe('CompetitionPlayerComponent', () => {
  let component: CompetitionPlayerComponent;
  let fixture: ComponentFixture<CompetitionPlayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompetitionPlayerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompetitionPlayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
