import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentProgressComponent } from './incident-progress.component';

describe('IncidentProgressComponent', () => {
  let component: IncidentProgressComponent;
  let fixture: ComponentFixture<IncidentProgressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentProgressComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
