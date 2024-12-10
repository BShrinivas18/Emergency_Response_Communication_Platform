import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SosAlertModalComponent } from './sos-alert-modal.component';

describe('SosAlertModalComponent', () => {
  let component: SosAlertModalComponent;
  let fixture: ComponentFixture<SosAlertModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SosAlertModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SosAlertModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
