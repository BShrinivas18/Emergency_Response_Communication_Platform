// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-incident-log-table',
//   imports: [],
//   templateUrl: './incident-log-table.component.html',
//   styleUrl: './incident-log-table.component.css'
// })
// export class IncidentLogTableComponent {

// }

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  standalone:true,
  selector: 'app-incident-log-table',
  imports:[CommonModule],
  templateUrl: './incident-log-table.component.html',
  styleUrls: ['./incident-log-table.component.css']
})
export class IncidentLogTableComponent {
  incidentLogs = [
    { id: 1, type: 'Fire', location: 'Downtown', status: 'Active', timestamp: '2023-06-01 10:00 AM' },
    { id: 2, type: 'Medical Emergency', location: 'Suburb', status: 'Resolved', timestamp: '2023-06-01 11:30 AM' },
    { id: 3, type: 'Traffic Accident', location: 'Highway', status: 'In Progress', timestamp: '2023-06-01 12:45 PM' },
    { id: 4, type: 'Hazardous Material Spill', location: 'Industrial Park', status: 'Active', timestamp: '2023-06-01 02:15 PM' },
    { id: 5, type: 'Search and Rescue', location: 'Mountain Area', status: 'In Progress', timestamp: '2023-06-01 03:30 PM' },
  ];
}

