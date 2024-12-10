

import { Component } from '@angular/core';
import { IncidentComponent } from '../active-incidents/active-incidents.component';
import { IncidentHistoryComponent } from '../past-incidents/past-incidents.component';
import { ProfileHeaderComponent } from '../profile-card/profile-card.component';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-responder-dashboard',
  standalone: true,
  imports: [IncidentHistoryComponent, ProfileHeaderComponent, ReactiveFormsModule,IncidentComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class ResponderDashboardComponent {}
