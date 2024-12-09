// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-incident-report',
//   imports: [],
//   templateUrl: './incident-report.component.html',
//   styleUrl: './incident-report.component.css'
// })
// export class IncidentReportComponent {

// }

import { Component, Input } from '@angular/core';
import { IncidentReport } from '../../../shared/models/incident.model';
import { CommonModule } from '@angular/common';
import { EmergencyService } from '../../../core/services/emergency.service';
@Component({
  selector: 'app-incident-report',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incident-report.component.html',
  styleUrls: ['./incident-report.component.css']
})
export class IncidentReportComponent {
  incident!: any;
  constructor(private emergencyService: EmergencyService) {
    
  }
 ngOnInit() {
    console.log("From incident report Component");
    
    this.incident = this.emergencyService.getFormData();
    // console.log(this.incident);
    // console.log(this.incident.incidentLocation);

  }
}
