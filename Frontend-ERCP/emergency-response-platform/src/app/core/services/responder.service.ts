// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root',
// })
// export class ResponderService {
//   private responders = [
//     { id: 1, name: 'John Doe', type: 'FIREFIGHTER', status: 'AVAILABLE', stationLocation: 'Central Station', contact: 'john.doe@example.com', yearsOfExperience: 5 },
//     { id: 2, name: 'Jane Smith', type: 'FIREFIGHTER', status: 'ON_ROUTE', stationLocation: 'North Station', contact: 'jane.smith@example.com', yearsOfExperience: 8 },
//     { id: 3, name: 'Mike Johnson', type: 'PARAMEDIC', status: 'ASSIGNED', stationLocation: 'Central Hospital', contact: 'mike.johnson@example.com', yearsOfExperience: 3 },
//     { id: 4, name: 'Jada',type: 'POLICE_OFFICER', status: 'UNAVAILABLE', stationLocation: 'Central Station', contact: 'jada@example.com', yearsOfExperience: 2 },
//     { id: 5, name: 'Jada Pink',type: 'RESCUE_TEAM', status: 'UNAVAILABLE', stationLocation: 'Central Station', contact: 'jada@example.com', yearsOfExperience: 2 },
//     { id: 6, name: 'Will Smith',type: 'RESCUE_TEAM', status: 'UNAVAILABLE', stationLocation: 'Central Station', contact: 'jada@example.com', yearsOfExperience: 2 },
//     { id: 7, name: 'Mark',type: 'HAZMAT_SPECIALIST', status: 'AVAILABLE', stationLocation: 'Central Station', contact: 'jada@example.com', yearsOfExperience: 2 },
//   ];

//   getRespondersByType(type: string) {
//     return this.responders.filter(responder => responder.type === type);
//   }

//   getResponderById(id: number) {
//     return this.responders.find(responder => responder.id === id);
//   }

//   updateResponder(updatedResponder: any) {
//     const index = this.responders.findIndex(r => r.id === updatedResponder.id);
//     if (index !== -1) this.responders[index] = updatedResponder;
//   }
// }
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';

// export interface ResponderDTO {
//   responderId: number;
//   name: string;
//   stationLocation: string;
//   status: 'AVAILABLE' | 'UNAVAILABLE' | string;
//   type: 'FIREFIGHTER' | 'PARAMEDIC' | 'POLICE_OFFICER' | 'HAZMAT_SPECIALIST' | 'RESCUE_TEAM';
//   lastUpdate: string;
//   locationId?: number;
//   incidentId?: number;
// }

// @Injectable({
//   providedIn: 'root'
// })
// export class ResponderService {
//   private apiUrl = 'http://localhost:8888/responders';

//   constructor(private http: HttpClient) { }

//   getAvailableResponders(): Observable<ResponderDTO[]> {
//     return this.http.get<ResponderDTO[]>(`${this.apiUrl}/status/AVAILABLE`);
//   }
// }

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

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

  getResponderById(id: number): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    // console.log(headers.get+" coming from get responder by id");
    return this.http.get<ResponderDTO>(`${this.apiUrl}/${id}`, {headers});
  }

  createResponder(responder: ResponderDTO): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    console.log("token is "+token);
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      // 'Content-Type': 'application/json'
    });
    console.log("Coming to create responder");
    console.log(headers);
    return this.http.post<ResponderDTO>(`${this.apiUrl}/create`,  new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      // 'Content-Type': 'application/json'
    }));
  }

  updateResponder(id: number, responder: ResponderDTO): Observable<ResponderDTO> {
    const token = sessionStorage.getItem('jwt');
    console.log("Coming to update responder");
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.put<ResponderDTO>(`${this.apiUrl}/${id}`, responder, {headers});
  }
}
