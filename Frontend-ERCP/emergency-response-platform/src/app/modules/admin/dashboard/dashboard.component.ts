// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-dashboard',
//   imports: [],
//   templateUrl: './dashboard.component.html',
//   styleUrl: './dashboard.component.css'
// })
// export class DashboardComponent {

// }

import { Component } from '@angular/core';
import { IncidentLogTableComponent } from './incident-log-table/incident-log-table.component';
import { ResponderTableComponent } from './responder-table/responder-table.component';
import { CommonModule } from '@angular/common';


@Component({
  standalone:true,
  imports:[IncidentLogTableComponent,ResponderTableComponent,CommonModule],
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class AdminDashboardComponent {}

