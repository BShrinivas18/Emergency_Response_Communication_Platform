// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class IncidentManagementService {

//   constructor() { }
// }
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class IncidentManagementService {
  private incidents = [
    { id: 1, type: 'Fire', location: 'Downtown', status: 'Active', date: '2023-06-01' },
    { id: 2, type: 'Medical', location: 'Suburb', status: 'Resolved', date: '2023-05-30' },
    { id: 3, type: 'Traffic Accident', location: 'Highway', status: 'Active', date: '2023-06-02' },
  ];

  private logs = [
    { id: 1, timestamp: '2023-06-01 10:00', message: 'Incident reported' },
    { id: 2, timestamp: '2023-06-01 10:05', message: 'Responders dispatched' },
    { id: 3, timestamp: '2023-06-01 10:15', message: 'Responders arrived on scene' },
    { id: 4, timestamp: '2023-06-01 10:30', message: 'Situation assessment completed' },
    { id: 5, timestamp: '2023-06-01 11:00', message: 'Additional resources requested' },
  ];

  getIncidents(): Observable<any[]> {
    return of(this.incidents);
  }

  getIncidentLogs(incidentId: number): Observable<any[]> {
    // Here we could associate logs with incidents if applicable.
    return of(this.logs);
  }
}

