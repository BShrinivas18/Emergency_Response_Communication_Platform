
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ResponderService, ResponderDTO, ResponderStatus, ResponderType } from '../../../core/services/responder.service';
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
  responder: ResponderDTO = {} as ResponderDTO;
  incident: Incident = {} as Incident;
  address!: string;
  status!: string;
  currentIncidentId!: number;
  type!: ResponderType;
  responderTypes: ResponderType[] = [ResponderType.PARAMEDIC, ResponderType.FIREFIGHTER, ResponderType.POLICE_OFFICER, ResponderType.HAZMAT_SPECIALIST, ResponderType.RESCUE_TEAM];
  constructor(private fb: FormBuilder, private  responderService: ResponderService, private incidentService: IncidentManagementService) {

    // this,
    this.responderForm = this.fb.group({
      responderType: [, [Validators.required]]
    });
  }
  ngOnInit(): void {
    this.fetchResponderData();
  }

  fetchResponderData(): void {
    
    this.responderService.getResponderById(Number(sessionStorage.getItem('responderId'))).subscribe({
      next: (responder) => {
        this.responder = responder
        console.log('Responder data fetched: ', this.responder);
        console.log('Responders incident ID:', this.responder.incidentId);
        if(this.responder.incidentId){
        this.incidentService.getIncidentById(this.responder.incidentId).subscribe({
          next: (incident) => {
            this.incident = incident;
            // console.log('Incident data fetched:', this.incident);
            // console.log('Incident ID:', Number(this.incident.incidentId));
            // console.log('Incident type:', this.incident.type);
            // console.log('Incident victim name:', this.incident.victimName);
            // console.log('Incident victim contact:', this.incident.victimContact);
            // console.log('Incident responder Id:', this.incident.responderIds);
            // console.log('Incident Status', this.incident.status);
            this.status = this.incident.status;
            this.currentIncidentId = this.incident.incidentId;
            // console.log('Incident responder address:', this.incident.incidentLocation);
            
          },
          error: (error) => {
            console.error('Error fetching incident data:', error);
          }
        });
      }
        // Convert the responder's status to string and set
      //  this.address = this.locationService.getAddressFromCoordi
      },
      error: (error) => {
        console.error('Error fetching responder data:', error);
      }
    });
    
    
  }
  requestResponders() {
    
    this.responderService.requestAdditionalResponders(this.currentIncidentId, this.type ).subscribe({
      next: (responders) => {
        console.log('Responders requested: ', responders);
      },
      error: (error) => {
        console.error('Error requesting responders:', error);
      }
    });
  }

}