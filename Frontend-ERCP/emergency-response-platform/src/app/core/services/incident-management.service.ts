
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { User } from '../../shared/models/user.model';  
import { Incident } from '../../shared/models/incident.model';
@Injectable({
  providedIn: 'root'
})
export class IncidentManagementService {
  private baseUrl = 'http://localhost:8888';

  constructor(private http: HttpClient) {}

  // Method to get JWT token from local storage
  private getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('jwt');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  // getUserById(id: number): Observable<User> {
  //   return this.http.get<any>(`${this.baseUrl}/user`, { 
  //     headers: this.getAuthHeaders() 
  //   }).pipe(
  //     catchError(this.handleError)
  //   );
  // }

  // getUserId(): Observable<string|null> {
  //  const username = sessionStorage.getItem('username');
  //  const token = sessionStorage.getItem('jwt');
   
  //  const headers = this.getAuthHeaders();
  
  // //  headers.append('username', username);
  //  return this.http.get<any>(`${this.baseUrl}/user/${username}`, { 
  //    headers: headers
  //  }).pipe(
  //    map((user: any) => user?.id ?? null),
  //    catchError(this.handleError)
  //  );

  // }
  // Get all incidents
  getIncidents(): Observable<Incident[]> {
   
    return this.http.get<Incident[]>(`${this.baseUrl}/incidents`, { 
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
    const token = sessionStorage.getItem('jwt');
    
    console.log('Fetching incidents with headers:', this.getAuthHeaders());

    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    })
    return this.http.get<any[]>(`${this.baseUrl}/logs/incident/${incidentId}`, { 
      headers
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
  getIncidentById(id: number): Observable<Incident> {
    console.log("Coming to get incident by id");
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    })
    return this.http.get<Incident>(`${this.baseUrl}/incidents/${id}`, { 
      headers
    }).pipe(
      catchError(this.handleError)
    );
  }
}

