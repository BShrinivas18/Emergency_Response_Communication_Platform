import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentSubmissionConfirmationComponent } from './incident-submission-confirmation.component';

describe('IncidentSubmissionConfirmationComponent', () => {
  let component: IncidentSubmissionConfirmationComponent;
  let fixture: ComponentFixture<IncidentSubmissionConfirmationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentSubmissionConfirmationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentSubmissionConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
