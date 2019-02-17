import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameDetailCardComponent } from './game-detail-card.component';

describe('GameDetailCardComponent', () => {
  let component: GameDetailCardComponent;
  let fixture: ComponentFixture<GameDetailCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GameDetailCardComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameDetailCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
