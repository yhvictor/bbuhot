import { async, TestBed } from '@angular/core/testing';
import { RoutingComponent } from './routing.component';

describe('RoutingComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RoutingComponent]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(RoutingComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'Gotham-Spinach'`, () => {
    const fixture = TestBed.createComponent(RoutingComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('Gotham-Spinach');
  });

  it('should render title in a h1 tag', () => {
    const fixture = TestBed.createComponent(RoutingComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to Gotham-Spinach!');
  });
});
