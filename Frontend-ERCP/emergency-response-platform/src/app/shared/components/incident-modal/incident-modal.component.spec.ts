import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentReportModalComponent } from './incident-modal.component';

describe('IncidentReportModalComponent', () => {
  let component: IncidentReportModalComponent;
  let fixture: ComponentFixture<IncidentReportModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentReportModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentReportModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
