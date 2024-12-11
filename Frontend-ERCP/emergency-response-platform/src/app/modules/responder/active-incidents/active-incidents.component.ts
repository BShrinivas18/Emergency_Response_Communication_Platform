
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ResponderService, ResponderDTO, ResponderStatus } from '../../../core/services/responder.service';
import { Incident } from '../../../shared/models/incident.model';
import { IncidentManagementService } from '../../../core/services/incident-management.service';
@Component({
  selector: 'app-incident',
  standalone: true,
  imports: [ CommonModule, ReactiveFormsModule],
  templateUrl: './active-incidents.component.html', 
  styleUrls: ['./active-incidents.component.css']
})
export class IncidentComponent {
  responderForm: FormGroup;
  responder!: ResponderDTO;
  incident!:Incident;
  address!: string;
  status!: string;
  constructor(private fb: FormBuilder, private  responderService: ResponderService, private incidentService: IncidentManagementService) {

    this,
    this.responderForm = this.fb.group({
      responderCount: [1, [Validators.required, Validators.min(1)]]
    });
  }
  ngOnInit(): void {
    this.fetchResponderData();
  }

  fetchResponderData(): void {
    this.responderService.getResponderById(Number(sessionStorage.getItem('userId'))).subscribe({
      next: (responder) => {
        this.responder = responder
        console.log('Responder data fetched: ', this.responder);
        console.log('Responders incident ID:', this.responder.incidentId);
        this.incidentService.getIncidentById(this.responder.incidentId).subscribe({
          next: (incident) => {
            this.incident = incident;
            console.log('Incident data fetched:', this.incident);
            console.log('Incident ID:', Number(this.incident.incidentId));
            console.log('Incident type:', this.incident.type);
            console.log('Incident victim name:', this.incident.victimName);
            console.log('Incident victim contact:', this.incident.victimContact);
            console.log('Incident responder Id:', this.incident.responderIds);
            console.log('Incident Status', this.incident.status);
            this.status = this.incident.status;
            console.log('Incident responder address:', this.incident.incidentLocation);
            
          },
          error: (error) => {
            console.error('Error fetching incident data:', error);
          }
        });
        // Convert the responder's status to string and set
      //  this.address = this.locationService.getAddressFromCoordi
      },
      error: (error) => {
        console.error('Error fetching responder data:', error);
      }
    });
  }
  requestResponders() {
    if (this.responderForm.valid) {
      const count = this.responderForm.get('responderCount')?.value;
      alert(`Requested ${count} additional responder(s)`);
    }
  }
}