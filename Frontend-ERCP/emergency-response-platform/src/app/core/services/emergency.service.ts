
// // src/app/core/services/emergency.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { Incident, IncidentStatus, IncidentType,IncidentTypeMapping } from '../../shared/models/incident.model';
import { IncidentSubmissionConfirmationComponent } from '../../shared/components/incident-submission-confirmation/incident-submission-confirmation.component';
@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  private apiUrl = 'http://localhost:8888/incidents'; // Replace with actual backend
  
  private incidentsSubject = new BehaviorSubject<Incident[]>([]);
  incidents$ = this.incidentsSubject.asObservable();

  constructor(private http: HttpClient,
    private dialog: MatDialog

    
  ) {}

  // reportIncident(incident: Incident): Observable<Incident> {
  //   const token = sessionStorage.getItem('jwt');
  //   const mappedType = IncidentTypeMapping[incident.type];


  //   const incidentData = {
  //     incidentLocation: {
  //       latitude:incident.incidentlocation.latitude,
  //       longitude: incident.incidentlocation.longitude,
  //       address: incident.incidentlocation.address
  //     },
  //     victimLocation: {
  //       latitude: incident.victimlocation.latitude,
  //       longitude: incident.victimlocation.longitude, 
  //       address: incident.victimlocation.address
  //     },
  //     type:mappedType,
  //     status: incident.status,
  //     timestamp: incident.timestamp,
  //     victimName: incident.victimName,
  //     victimContact: incident.victimContact,
  //   };
  //   // Create headers with Authorization
  //   const headers = new HttpHeaders({
  //     'Authorization': `Bearer ${token}`,
  //     'Content-Type': 'application/json'
  //   });

  //   // console.log("Coming");
  //   console.log("incident data being sent to backend");
  //   console.log(incidentData);
  //   console.log("incident Type : "+ incident.type);
  //   return this.http.post<Incident>(`http://localhost:8888/incidents/create`,incidentData,{headers});
  // }

  reportIncident(incident: Incident): Observable<Incident> {
    const token = sessionStorage.getItem('jwt');
    const mappedType = IncidentTypeMapping[incident.type];
  
    const incidentData = {
      incidentLocation: {
        latitude: incident.incidentlocation.latitude,
        longitude: incident.incidentlocation.longitude,
        address: incident.incidentlocation.address
      },
      victimLocation: {
        latitude: incident.victimlocation.latitude,
        longitude: incident.victimlocation.longitude, 
        address: incident.victimlocation.address
      },
      type: mappedType,
      status: incident.status,
      timestamp: new Date().toISOString(), // Ensure timestamp is in correct format
      victimName: incident.victimName || 'Anonymous',
      victimContact: incident.victimContact,
    };
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  
    console.log("Incident data being sent to backend:", incidentData);
    return this.http.post<Incident>(`http://localhost:8888/incidents/create`, incidentData, { headers });
  }

  // getIncidents(): Observable<Incident[]> {
  //   const token = sessionStorage.getItem('jwt');
    
  //   // Create headers with Authorization
  //   const headers = new HttpHeaders({
  //     'Authorization': `Bearer ${token}`,
  //     'Content-Type': 'application/json'
  //   });
  //   return this.http.get<Incident[]>(`${this.apiUrl}`, {headers});
  // }

  updateIncidentsList(incidents: Incident[]) {
    
    this.incidentsSubject.next(incidents);
  }

  sendSOSAlert(incident: Incident): Observable<Incident> {
    const token = sessionStorage.getItem('jwt');
    
    // Create headers with Authorization
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
    return this.http.post<Incident>(`${this.apiUrl}/sos`, {
      ...incident,
      incidentType: IncidentType.SOS_REQUEST,
      // status: IncidentStatus.REPORTED
      status: 'Reported'
    },
  {headers});
  }
  getFormData() {
    console.log("coming from get data method : ");
    console.log(this.incidentsSubject.value[0]);
    return this.incidentsSubject.value[0];
  }
  setFormData(incident: Incident) {
    console.log("coming from get data method : ");
    // console.log(incident);
    this.incidents$.subscribe(data => {
      data.push(incident);
      // console.log(data[0]);
    });
  }
  openIncidentConfirmationModal(incident: Incident) {
    // console.log(incident);
    return this.dialog.open(IncidentSubmissionConfirmationComponent, {
      // width: '400px',
      data: { incident }
    });
  }
}