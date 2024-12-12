// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-communication-log',
//   imports: [],
//   templateUrl: './communication-log.component.html',
//   styleUrl: './communication-log.component.css'
// })
// export class CommunicationLogComponent {

// }

import { Component, Input } from '@angular/core';
import { CommunicationEntry } from '../../../shared/models/incident.model';
import { CommonModule } from '@angular/common';
import { IncidentManagementService } from '../../../core/services/incident-management.service';
import { LogDTO } from '../../../core/services/log.service';
import { EmergencyService } from '../../../core/services/emergency.service';
@Component({
  selector: 'app-communication-log',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './communication-log.component.html',
  styleUrls: ['./communication-log.component.css']
})
export class CommunicationLogComponent {
  // @Input() entries!: CommunicationEntry[];
  logs: LogDTO[] = [];
  incidentId: number | any;
  constructor(private incidentService: IncidentManagementService, private emergencyService: EmergencyService) {
    
  }
  ngOnInit() {
  this.incidentId = this.emergencyService.getcurrentIncidentId();
  this.incidentService.getIncidentLogs(this.incidentId).subscribe({
    next: (data) => {
      this.logs = data;
    },
    error: (error) => {
      console.error('Error fetching incident logs:', error);
    }
  });
  
}
}
