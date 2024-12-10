import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommunicationLogComponent } from './communication-log.component';

describe('CommunicationLogComponent', () => {
  let component: CommunicationLogComponent;
  let fixture: ComponentFixture<CommunicationLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommunicationLogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommunicationLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
