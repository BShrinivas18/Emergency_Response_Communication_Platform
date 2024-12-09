// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class LogService {

//   constructor() { }
// }
// import { Injectable } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { Observable } from 'rxjs';

// export interface LogDTO {
//   logId: number;
//   statusUpdate: string;
//   timestamp: string;
//   incidentId: number;
// }

// @Injectable({
//   providedIn: 'root'
// })
// export class LogService {
//   private apiUrl = 'http://localhost:8888/logs';

//   constructor(private http: HttpClient) { }

//   getAllLogs(): Observable<LogDTO[]> {
//     return this.http.get<LogDTO[]>(this.apiUrl);
//   }
// }
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LogDTO {
  logId: number;
  statusUpdate: string;
  timestamp: string;
  incidentId: number;
}

@Injectable({
  providedIn: 'root'
})
export class LogService {
  private apiUrl = 'http://localhost:8888/logs';

  constructor(private http: HttpClient) { }

  getAllLogs(): Observable<LogDTO[]> {
    // Retrieve the JWT token from local storage
    const token = localStorage.getItem('jwtToken');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // Make the HTTP request with headers
    return this.http.get<LogDTO[]>(this.apiUrl, { headers });
  }
}