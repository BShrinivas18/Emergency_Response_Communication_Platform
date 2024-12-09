
// // src/app/core/services/emergency.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { Incident, IncidentStatus, IncidentType } from '../../shared/models/incident.model';
import { IncidentSubmissionConfirmationComponent } from '../../shared/components/incident-submission-confirmation/incident-submission-confirmation.component';
@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  private apiUrl = 'https://your-backend-api.com/incidents'; // Replace with actual backend
  
  private incidentsSubject = new BehaviorSubject<Incident[]>([]);
  incidents$ = this.incidentsSubject.asObservable();

  constructor(private http: HttpClient,
    private dialog: MatDialog
    
  ) {}

  reportIncident(incident: Incident): Observable<Incident> {
    return this.http.post<Incident>(`${this.apiUrl}/report`, {
      ...incident,
      // status: IncidentStatus.REPORTED
      status: 'Reported'
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
      // status: IncidentStatus.REPORTED
      status: 'Reported'
    });
  }
  getFormData() {
    console.log("coming from get data method : ");
    console.log(this.incidentsSubject.value[0]);
    return this.incidentsSubject.value[0];
  }
  setFormData(incident: Incident) {
    console.log("coming from get data method : ");
    console.log(incident);
    this.incidents$.subscribe(data => {
      data.push(incident);
      console.log(data[0]);
    });
  }
  openIncidentConfirmationModal(incident: Incident) {
    console.log(incident);
    return this.dialog.open(IncidentSubmissionConfirmationComponent, {
      // width: '400px',
      data: { incident }
    });
  }
}