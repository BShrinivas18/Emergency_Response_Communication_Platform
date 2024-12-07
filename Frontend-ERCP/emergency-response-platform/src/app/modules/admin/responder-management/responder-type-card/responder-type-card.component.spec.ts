import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponderTypeCardComponent } from './responder-type-card.component';

describe('ResponderTypeCardComponent', () => {
  let component: ResponderTypeCardComponent;
  let fixture: ComponentFixture<ResponderTypeCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponderTypeCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponderTypeCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
