import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentLogTableComponent } from './incident-log-table.component';

describe('IncidentLogTableComponent', () => {
  let component: IncidentLogTableComponent;
  let fixture: ComponentFixture<IncidentLogTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentLogTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentLogTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
