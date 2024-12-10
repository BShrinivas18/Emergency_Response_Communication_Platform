import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncidentTableComponent } from './incident-table.component';

describe('IncidentTableComponent', () => {
  let component: IncidentTableComponent;
  let fixture: ComponentFixture<IncidentTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IncidentTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IncidentTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
