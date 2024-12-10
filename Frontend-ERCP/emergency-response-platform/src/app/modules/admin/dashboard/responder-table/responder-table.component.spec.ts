import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponderTableComponent } from './responder-table.component';

describe('ResponderTableComponent', () => {
  let component: ResponderTableComponent;
  let fixture: ComponentFixture<ResponderTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponderTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponderTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
