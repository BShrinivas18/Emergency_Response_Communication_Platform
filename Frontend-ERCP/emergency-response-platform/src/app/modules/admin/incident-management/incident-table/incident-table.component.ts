// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-incident-table',
//   imports: [],
//   templateUrl: './incident-table.component.html',
//   styleUrl: './incident-table.component.css'
// })
// export class IncidentTableComponent {

// }

import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import {MatTable} from '@angular/material/table'
import { IncidentManagementService } from '../../../../core/services/incident-management.service';
import { MatTabLabel } from '@angular/material/tabs';
@Component({
  standalone:true,
  imports:[MatTable],
  selector: 'app-incident-table',
  templateUrl: './incident-table.component.html',
  styleUrls: ['./incident-table.component.css'],
})
export class IncidentTableComponent implements OnInit{
 
  @Input() status?: 'active' | 'resolved';
  @Output() selectIncident = new EventEmitter<number>();
  incidents1:any[] = [];

  incidents = [
    { id: 1, type: 'Fire', location: 'Downtown', status: 'Active', date: '2023-06-01' },
    { id: 2, type: 'Medical', location: 'Suburb', status: 'Resolved', date: '2023-05-30' },
    { id: 3, type: 'Traffic Accident', location: 'Highway', status: 'Active', date: '2023-06-02' },
  ];

  constructor(private incidentService:IncidentManagementService){}

  ngOnInit(): void {

    this.incidentService.getIncidents().subscribe((data) => {
      this.incidents = this.status
        ? data.filter((incident) => incident.status.toLowerCase() === this.status)
        : data;
    });
  }

  onViewLogs(id: number): void {
    this.selectIncident.emit(id);
  }

  


}

