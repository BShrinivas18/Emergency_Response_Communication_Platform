// import { Injectable } from '@angular/core';
// import { Observable, of } from 'rxjs';

// @Injectable({
//   providedIn: 'root',
// })
// export class IncidentManagementService {
//   private incidents = [
//     { id: 1, type: 'Fire', location: 'Downtown', status: 'Active', date: '2023-06-01' },
//     { id: 2, type: 'Medical', location: 'Suburb', status: 'Resolved', date: '2023-05-30' },
//     { id: 3, type: 'Traffic Accident', location: 'Highway', status: 'Active', date: '2023-06-02' },
//   ];

//   private logs = [
//     { id: 1, timestamp: '2023-06-01 10:00', message: 'Incident reported' },
//     { id: 2, timestamp: '2023-06-01 10:05', message: 'Responders dispatched' },
//     { id: 3, timestamp: '2023-06-01 10:15', message: 'Responders arrived on scene' },
//     { id: 4, timestamp: '2023-06-01 10:30', message: 'Situation assessment completed' },
//     { id: 5, timestamp: '2023-06-01 11:00', message: 'Additional resources requested' },
//   ];

//   getIncidents(): Observable<any[]> {
//     return of(this.incidents);
//   }

//   getIncidentLogs(incidentId: number): Observable<any[]> {
//     // Here we could associate logs with incidents if applicable.
//     return of(this.logs);
//   }
// }

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IncidentManagementService {
  private baseUrl = 'http://localhost:8888';

  constructor(private http: HttpClient) {}

  // Method to get JWT token from local storage
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwtToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // Get all incidents
  getIncidents(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/incidents`, { 
      headers: this.getAuthHeaders() 
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get incidents by status
  getIncidentsByStatus(status: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/incidents/status/${status}`, { 
      headers: this.getAuthHeaders() 
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get logs for a specific incident
  getIncidentLogs(incidentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/logs/incident/${incidentId}`, { 
      headers: this.getAuthHeaders() 
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Error handling method
  private handleError(error: any): Observable<never> {
    console.error('An error occurred:', error);
    // You might want to implement more sophisticated error handling
    throw error;
  }
}

