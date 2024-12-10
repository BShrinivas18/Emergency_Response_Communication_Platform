import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveIncidentsComponent } from './active-incidents.component';

describe('ActiveIncidentsComponent', () => {
  let component: ActiveIncidentsComponent;
  let fixture: ComponentFixture<ActiveIncidentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActiveIncidentsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveIncidentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
