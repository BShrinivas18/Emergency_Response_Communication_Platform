// import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
// import {MatTable, MatTableModule} from '@angular/material/table'
// import { IncidentManagementService } from '../../../../core/services/incident-management.service';
// import { MatTabLabel } from '@angular/material/tabs';
// import { CommonModule } from '@angular/common';
// import { MatButtonModule } from '@angular/material/button';
// @Component({
//   standalone:true,
//   imports:[CommonModule,MatTableModule,MatButtonModule],
//   selector: 'app-incident-table',
//   templateUrl: './incident-table.component.html',
//   styleUrls: ['./incident-table.component.css'],
// })
// export class IncidentTableComponent implements OnInit{
 
//   @Input() status?: 'active' | 'resolved';
//   @Output() selectIncident = new EventEmitter<number>();
//   incidents1:any[] = [];

//   incidents = [
//     { id: 1, type: 'Fire', location: 'Downtown', status: 'Active', date: '2023-06-01' },
//     { id: 2, type: 'Medical', location: 'Suburb', status: 'Resolved', date: '2023-05-30' },
//     { id: 3, type: 'Traffic Accident', location: 'Highway', status: 'Active', date: '2023-06-02' },
//   ];

//   constructor(private incidentService:IncidentManagementService){}

//   ngOnInit(): void {

//     this.incidentService.getIncidents().subscribe((data) => {
//       this.incidents = this.status
//         ? data.filter((incident) => incident.status.toLowerCase() === this.status)
//         : data;
//     });
//   }

//   onViewLogs(id: number): void {
//     this.selectIncident.emit(id);
//   }

  


// }

import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { IncidentManagementService } from '../../../../core/services/incident-management.service';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule],
  selector: 'app-incident-table',
  templateUrl: './incident-table.component.html',
  styleUrls: ['./incident-table.component.css'],
})
export class IncidentTableComponent implements OnInit {
  @Input() status?: string;
  @Output() selectIncident = new EventEmitter<number>();

  displayedColumns: string[] = ['type', 'incidentLocationId', 'status', 'timestamp', 'action'];
  incidents: any[] = [];

  constructor(private incidentService: IncidentManagementService) {}

  ngOnInit(): void {
    if (this.status) {
      // For 'Active' tab, fetch 'NEW' status
      // For 'Resolved' tab, fetch 'RESOLVED' status
      const fetchStatus = this.status === 'active' ? 'NEW' : 
                          this.status === 'resolved' ? 'RESOLVED' : null;
      
      if (fetchStatus) {
        this.incidentService.getIncidentsByStatus(fetchStatus).subscribe({
          next: (data) => {
            this.incidents = data;
          },
          error: (error) => {
            console.error('Error fetching incidents:', error);
          }
        });
      } else {
        // For 'All Incidents'
        this.incidentService.getIncidents().subscribe({
          next: (data) => {
            this.incidents = data;
          },
          error: (error) => {
            console.error('Error fetching incidents:', error);
          }
        });
      }
    } else {
      // Default case for 'All Incidents'
      this.incidentService.getIncidents().subscribe({
        
        next: (data) => {
          this.incidents = data;
        },
        error: (error) => {
          console.error('Error fetching incidents:', error);
        }
      });
    }
  }
  
  onViewLogs(incidentId: number): void {
    this.selectIncident.emit(incidentId);
  }
}

