import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentLogsComponent } from './incident-logs.component';

describe('IncidentLogsComponent', () => {
  let component: IncidentLogsComponent;
  let fixture: ComponentFixture<IncidentLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentLogsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
