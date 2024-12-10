// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-dashboard',
//   imports: [],
//   templateUrl: './dashboard.component.html',
//   styleUrl: './dashboard.component.css'
// })
// export class DashboardComponent {

// }




















// import { Component, Input, OnInit } from '@angular/core';
// import { IncidentReport, Stage, CommunicationEntry } from '../../models/incident.model';

// @Component({
//   selector: 'app-incident-dashboard',
//   templateUrl: './incident-dashboard.component.html',
//   styleUrls: ['./incident-dashboard.component.css']
// })
// export class IncidentDashboardComponent implements OnInit {
//   @Input() initialIncident!: IncidentReport;
  
//   incident!: IncidentReport;
//   stages: Stage[] = [
//     { name: 'Incident Reported', status: 'Reported', icon: 'report', completedAt: new Date(Date.now() - 3600000) },
//     { name: 'Responder Assigned', status: 'Assigned', icon: 'person', completedAt: new Date(Date.now() - 2400000) },
//     { name: 'Responder Enroute', status: 'Enroute', icon: 'directions_car', completedAt: new Date(Date.now() - 1200000) },
//     { name: 'Responder On Scene', status: 'OnScene', icon: 'place' },
//     { name: 'Incident Resolved', status: 'Resolved', icon: 'check_circle' }
//   ];

//   communicationLog: CommunicationEntry[] = [
//     { id: '1', timestamp: new Date(Date.now() - 3600000), sender: 'Dispatch', message: 'Incident reported, assigning responder.' },
//     { id: '2', timestamp: new Date(Date.now() - 2400000), sender: 'John Doe', message: 'Responder assigned. En route to the scene.' },
//     { id: '3', timestamp: new Date(Date.now() - 1800000), sender: 'Dispatch', message: 'Understood. What\'s your ETA?' },
//     { id: '4', timestamp: new Date(Date.now() - 1600000), sender: 'John Doe', message: 'ETA 10 minutes.' },
//     { id: '5', timestamp: new Date(Date.now() - 1200000), sender: 'John Doe', message: 'Arrived on scene. Assessing situation.' },
//   ];

//   ngOnInit() {
//     this.incident = { ...this.initialIncident };
//   }

//   sampleIncident: IncidentReport = {
//     id: 'INC-2024-001',
//     description: 'Traffic accident on Highway 101',
//     location: 'Highway 101, Mile Marker 45',
//     reportedAt: new Date(),
//     status: 'Assigned',
//     priority: 'High'
//   };
// }












import { Component, OnInit } from '@angular/core';
import { IncidentReport, Stage, CommunicationEntry } from '../../../shared/models/incident.model';
import { IncidentReportComponent } from '../incident-report/incident-report.component';
import { IncidentProgressComponent } from '../incident-progress/incident-progress.component'; 
import { CommunicationLogComponent } from '../communication-log/communication-log.component';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [IncidentReportComponent, IncidentProgressComponent, CommunicationLogComponent, CommonModule, ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  // Sample incident data directly in the dashboard component
  sampleIncident: IncidentReport = {
    id: 'INC-2024-001',
    description: 'Traffic accident on Highway 101',
    location: 'Highway 101, Mile Marker 45',
    reportedAt: new Date(),
    status: 'Assigned',
    priority: 'High'
  };
  

  stages: Stage[] = [
    { name: 'Incident Reported', status: 'Reported', icon: 'report', completedAt: new Date(Date.now() - 3600000) },
    { name: 'Responder Assigned', status: 'Assigned', icon: 'person', completedAt: new Date(Date.now() - 2400000) },
    { name: 'Responder Enroute', status: 'Enroute', icon: 'directions_car', completedAt: new Date(Date.now() - 1200000) },
    { name: 'Responder On Scene', status: 'OnScene', icon: 'place' },
    { name: 'Incident Resolved', status: 'Resolved', icon: 'check_circle' }
  ];

  communicationLog: CommunicationEntry[] = [
    { id: '1', timestamp: new Date(Date.now() - 3600000), sender: 'Dispatch', message: 'Incident reported, assigning responder.' },
    { id: '2', timestamp: new Date(Date.now() - 2400000), sender: 'John Doe', message: 'Responder assigned. En route to the scene.' },
    { id: '3', timestamp: new Date(Date.now() - 1800000), sender: 'Dispatch', message: 'Understood. What\'s your ETA?' },
    { id: '4', timestamp: new Date(Date.now() - 1600000), sender: 'John Doe', message: 'ETA 10 minutes.' },
    { id: '5', timestamp: new Date(Date.now() - 1200000), sender: 'John Doe', message: 'Arrived on scene. Assessing situation.' },
  ];

  ngOnInit() {
    
    // Any initialization logic can go here
  }
}