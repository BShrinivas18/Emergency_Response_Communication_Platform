import { API_ORIGIN } from '../api.config';

// // src/app/core/services/emergency.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, timeout, map } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { Incident, IncidentStatus, IncidentType,IncidentTypeMapping } from '../../shared/models/incident.model';
import { IncidentSubmissionConfirmationComponent } from '../../shared/components/incident-submission-confirmation/incident-submission-confirmation.component';
@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  private apiUrl = `${API_ORIGIN}/incidents`; // Replace with actual backend
  
  private incidentsSubject = new BehaviorSubject<Incident[]>([]);
  incidents$ = this.incidentsSubject.asObservable();

  currentIncidentId! : number;
  constructor(private http: HttpClient,
    private dialog: MatDialog,
    
  ) {
    
  }

  reportIncident(incident: Incident): Observable<Incident> {
    const payload = {
      type: IncidentTypeMapping[incident.type] || incident.type,
      incidentLocation: incident.incidentLocation,
      victimLocation: incident.victimLocation,
      victimName: incident.victimName || null,
      victimContact: incident.victimContact || null
    };
    return this.http.post<Incident>(`${this.apiUrl}/create`, payload).pipe(
      timeout(15000),
      map(receipt => {
        if (!Number.isInteger(receipt.incidentId) || receipt.incidentId <= 0) {
          throw new Error('Incident receipt not confirmed');
        }
        return receipt;
      }),
      tap(receipt => this.currentIncidentId = receipt.incidentId)
    );
  }

  getcurrentIncidentId(){
    return this.currentIncidentId;
  }
 
  updateIncidentsList(incidents: Incident[]) {
    
    
    this.incidentsSubject.next(incidents);
  }

  sendSOSAlert(incident: Incident): Observable<Incident> {
    return this.reportIncident({...incident, type: IncidentType.SOS_REQUEST});
  }
  getFormData() {


    return this.incidentsSubject.value[0];
  }
  setFormData(incident: Incident) { this.incidentsSubject.next([incident]); }
  openIncidentConfirmationModal(incident: Incident) {

    return this.dialog.open(IncidentSubmissionConfirmationComponent, {
      // width: '400px',
      data: { incident }
    });
  }
}
