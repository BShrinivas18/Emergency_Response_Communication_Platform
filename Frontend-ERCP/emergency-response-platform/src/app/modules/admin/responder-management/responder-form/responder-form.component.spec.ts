import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponderFormComponent } from './responder-form.component';

describe('ResponderFormComponent', () => {
  let component: ResponderFormComponent;
  let fixture: ComponentFixture<ResponderFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponderFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponderFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
