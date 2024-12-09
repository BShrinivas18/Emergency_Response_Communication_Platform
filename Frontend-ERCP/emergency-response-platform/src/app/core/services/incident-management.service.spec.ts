import { TestBed } from '@angular/core/testing';

import { IncidentManagementService } from './incident-management.service';

describe('IncidentManagementService', () => {
  let service: IncidentManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IncidentManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
