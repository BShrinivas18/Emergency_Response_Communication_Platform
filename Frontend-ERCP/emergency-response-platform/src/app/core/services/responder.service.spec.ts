import { TestBed } from '@angular/core/testing';

import { ResponderService } from './responder.service';

describe('ResponderService', () => {
  let service: ResponderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResponderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
