// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-dashboard',
//   imports: [],
//   templateUrl: './dashboard.component.html',
//   styleUrl: './dashboard.component.css'
// })
// export class DashboardComponent {

// }

import { Component, Input } from '@angular/core';
import { IncidentLogTableComponent } from './incident-log-table/incident-log-table.component';
import { ResponderTableComponent } from './responder-table/responder-table.component';
import { CommonModule } from '@angular/common';
import { ResponderService } from '../../../core/services/responder.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { IncidentManagementService } from '../../../core/services/incident-management.service';

@Component({
  standalone:true,
  imports:[IncidentLogTableComponent,ResponderTableComponent,CommonModule],
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class AdminDashboardComponent {
  numberOfResponders: number = 0;  // Initialize with 0
  activeIncidents: number = 0;
  constructor(private responderService: ResponderService, private incidentService: IncidentManagementService) {}

  ngOnInit(): void {
    // Subscribe to the observable to get the list of available responders
    this.responderService.getAvailableResponders().subscribe((responders) => {
      // Calculate the number of responders by getting the length of the array
      this.numberOfResponders = responders.length;
    });
    this.incidentService.getIncidents().subscribe((incidents) => {
      this.activeIncidents = incidents.length;
    });



    // console.log(this.incidentService.getIncidents());
  }
}

