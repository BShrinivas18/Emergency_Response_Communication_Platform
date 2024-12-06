
// // src/app/core/services/emergency.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Incident, IncidentStatus, IncidentType } from '../../shared/models/incident.model';

@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  private apiUrl = 'https://your-backend-api.com/incidents'; // Replace with actual backend
  
  private incidentsSubject = new BehaviorSubject<Incident[]>([]);
  incidents$ = this.incidentsSubject.asObservable();

  constructor(private http: HttpClient) {}

  reportIncident(incident: Incident): Observable<Incident> {
    return this.http.post<Incident>(`${this.apiUrl}/report`, {
      ...incident,
      status: IncidentStatus.REPORTED
    });
  }

  getIncidents(): Observable<Incident[]> {
    return this.http.get<Incident[]>(`${this.apiUrl}/list`);
  }

  updateIncidentsList(incidents: Incident[]) {
    this.incidentsSubject.next(incidents);
  }

  sendSOSAlert(incident: Incident): Observable<Incident> {
    return this.http.post<Incident>(`${this.apiUrl}/sos`, {
      ...incident,
      incidentType: IncidentType.MEDICAL,
      status: IncidentStatus.REPORTED
    });
  }
}