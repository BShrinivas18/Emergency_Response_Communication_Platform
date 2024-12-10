// import { IncidentModel } from './incident.model';

// describe('IncidentModel', () => {
//   it('should create an instance', () => {
//     expect(new IncidentModel()).toBeTruthy();
//   });
// });

import { Incident, IncidentStatus,IncidentType } from './incident.model';  // Corrected import

describe('Incident', () => {
  it('should create an instance', () => {
    const incident: Incident = {
      victimName: 'John Doe',
      victimContact: '1234567890',
      location: { latitude: 40.7128, longitude: -74.0060 },
      incidentType: IncidentType.FIRE,
      status: IncidentStatus.REPORTED
    };
    expect(incident).toBeTruthy();
  });
});

