import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogRef } from '@angular/material/dialog';
import { ResponderService, ResponderDTO, ResponderStatus, ResponderType } from '../../../../core/services/responder.service';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-responder-form',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatSelectModule, 
    MatButtonModule,
    MatCardModule
  ],
  templateUrl: './responder-form.component.html',
  styleUrls: ['./responder-form.component.css']
})
export class ResponderFormComponent implements OnInit {
  @Input() responder?: ResponderDTO;
  @Output() submit = new EventEmitter<ResponderDTO>();
  
  name: string = '';
  stationLocation: string = '';
  status: ResponderStatus = ResponderStatus.AVAILABLE;
  type: ResponderType = ResponderType.PARAMEDIC;
  locationId: number | null = null;

  RESPONDER_STATUSES = Object.values(ResponderStatus);
  RESPONDER_TYPES = Object.values(ResponderType);

  constructor(
    private dialogRef: MatDialogRef<ResponderFormComponent>,
    private responderService: ResponderService
  ) {}

  ngOnInit() {
    if (this.responder) {
      // this.responderId = this.responder.responderId;
      this.name = this.responder.name || '';
      this.stationLocation = this.responder.stationLocation || '';
      this.status = this.responder.status || ResponderStatus.AVAILABLE;
      this.type = this.responder.type || ResponderType.PARAMEDIC;
      this.locationId = this.responder.locationId || null;
    }
  }

  handleSubmit() {
    const responderData: ResponderDTO = {
      responderId: this.responder ? this.responder.responderId : 0, // Use existing ID if updating
      name: this.name,
      stationLocation: this.stationLocation,
      status: this.status,
      type: this.type,
      lastUpdate: new Date(),
      locationId: this.locationId || undefined, // Optional chaining
      incidentId: this.responder ? (this.responder.incidentId || 0) : 0
    };
  
    if (this.responder && this.responder.responderId) {
      // Updating existing responder
      this.responderService.updateResponder(this.responder.responderId, responderData)
        .subscribe({
          next: (updatedResponder) => {
            console.log('Responder updated successfully:', updatedResponder);
            this.dialogRef.close(updatedResponder);
          },
          error: (error) => {
            console.error('Error updating responder', error);
            // Optionally show an error message to the user
          }
        });
    } else {
      // Creating new responder
      this.responderService.createResponder(responderData)
        .subscribe({
          next: (newResponder) => {
            console.log('New responder created successfully:', newResponder);
            this.dialogRef.close(newResponder);
          },
          error: (error) => {
            console.error('Error creating responder', error);
            // Optionally show an error message to the user
          }
        });
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}