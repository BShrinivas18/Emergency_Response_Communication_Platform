import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentManagementComponent } from './incident-management.component';

describe('IncidentManagementComponent', () => {
  let component: IncidentManagementComponent;
  let fixture: ComponentFixture<IncidentManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentManagementComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
