
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
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    // Make the HTTP request with headers
    return this.http.get<LogDTO[]>(this.apiUrl, { headers });
  }
}