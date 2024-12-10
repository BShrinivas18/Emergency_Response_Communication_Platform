import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PastIncidentsComponent } from './past-incidents.component';

describe('PastIncidentsComponent', () => {
  let component: PastIncidentsComponent;
  let fixture: ComponentFixture<PastIncidentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PastIncidentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PastIncidentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
