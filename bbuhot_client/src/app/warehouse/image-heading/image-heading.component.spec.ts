import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageHeadingComponent } from './image-heading.component';

describe('ImageHeadingComponent', () => {
  let component: ImageHeadingComponent;
  let fixture: ComponentFixture<ImageHeadingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ImageHeadingComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageHeadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
