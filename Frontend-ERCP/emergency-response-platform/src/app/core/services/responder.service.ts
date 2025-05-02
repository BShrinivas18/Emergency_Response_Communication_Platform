import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../shared/models/user.model';

export interface ResponderDTO {
  responderId: number;
  name: string;
  stationLocation: string;
  status: ResponderStatus;
  type: ResponderType;
  lastUpdate?: Date;
  locationId?: number;
  incidentId: number|0;
}

export enum ResponderStatus {
  AVAILABLE = 'AVAILABLE',
  ASSIGNED = 'ASSIGNED',
  ON_ROUTE = 'ON_ROUTE',
  REACHED_LOCATION = 'REACHED_LOCATION',
  OFFLINE = 'OFFLINE',
  ON_SCENE = 'ON_SCENE',
  NOT_AVAILABLE = 'NOT_AVAILABLE',
  OFF_DUTY = 'OFF_DUTY'
}

export enum ResponderType {
  PARAMEDIC = 'PARAMEDIC',
  FIREFIGHTER = 'FIREFIGHTER',
  POLICE_OFFICER = 'POLICE_OFFICER',
  HAZMAT_SPECIALIST = 'HAZMAT_SPECIALIST',
  RESCUE_TEAM = 'RESCUE_TEAM'
}

@Injectable({
  providedIn: 'root'
})
export class ResponderService {
  private apiUrl = 'http://localhost:8888/responders';

  constructor(private http: HttpClient) { }

  getAvailableResponders(): Observable<ResponderDTO[]> {
    // Retrieve the JWT token from local storage
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // Make the HTTP request with headers
    return this.http.get<ResponderDTO[]>(`${this.apiUrl}/status/AVAILABLE`, { headers });
  }
  
  private getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('jwt');
    return new HttpHeaders({
      // 'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getRespondersByType(type: ResponderType): Observable<ResponderDTO[]> {
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.get<ResponderDTO[]>(`${this.apiUrl}/type/${type}`,{headers});
  }

  getresponderByUsername(username: string): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.get<ResponderDTO>(`${this.apiUrl}/username/${username}`,{headers});
  }

  getResponderById(id: number): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    // console.log({id});
    // console.log(headers.get+" coming from get responder by id");
    return this.http.get<ResponderDTO>(`${this.apiUrl}/${id}`, {headers});

  }
  createResponder(responder: ResponderDTO): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
  
    return this.http.post<ResponderDTO>(`${this.apiUrl}/create`, responder, { headers });
  }

  updateResponder(id: number, responder: ResponderDTO): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    
    console.log('Updating responder:', responder);
    
    return this.http.put<ResponderDTO>(`${this.apiUrl}/${id}`, responder, { headers });
  }

  requestAdditionalResponders(incidentId: number, type: ResponderType): Observable<ResponderDTO[]> {
    const token = sessionStorage.getItem('jwt');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
     return this.http.put<ResponderDTO[]>(`${this.apiUrl}/request-additional/${incidentId}/${type}`, {}, { headers });
  }

}
