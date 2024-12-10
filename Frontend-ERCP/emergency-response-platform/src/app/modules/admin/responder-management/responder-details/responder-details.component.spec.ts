import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponderDetailsComponent } from './responder-details.component';

describe('ResponderDetailsComponent', () => {
  let component: ResponderDetailsComponent;
  let fixture: ComponentFixture<ResponderDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponderDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
