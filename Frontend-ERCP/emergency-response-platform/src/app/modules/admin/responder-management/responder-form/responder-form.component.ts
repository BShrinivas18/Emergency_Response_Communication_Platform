// import { CommonModule } from '@angular/common';
// import { Component, Input, Output, EventEmitter } from '@angular/core';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatCardModule } from '@angular/material/card';
// import { MatSelectModule } from '@angular/material/select';
// import { FormsModule } from '@angular/forms';
// import { MatInputModule } from '@angular/material/input';
// import { MatButtonModule } from '@angular/material/button';
// import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';

// @Component({
//   selector: 'app-responder-form',
//   standalone: true,
//   imports: [MatFormFieldModule, 
//     CommonModule, MatCardModule, MatSelectModule, FormsModule,MatInputModule,MatButtonModule, MatDialogModule],
//   templateUrl: './responder-form.component.html',
//   styleUrls: ['./responder-form.component.css']
// })
// export class ResponderFormComponent {
//   @Input() responder?: any;
//   @Output() close = new EventEmitter<void>();
//   @Output() submit = new EventEmitter<any>();

//   constructor(private dialogRef: MatDialogRef<ResponderFormComponent>) {}

//   name: string = '';
//   stationLocation: string = '';
//   status: string = '';
//   type: string = '';
//   contact: string = '';
//   yearsOfExperience: number = 0;

//   RESPONDER_STATUSES = [
//     'AVAILABLE', 'ASSIGNED', 'ON_ROUTE', 'REACHED_LOCATION',
//     'OFFLINE', 'ON_SCENE', 'NOT_AVAILABLE', 'OFF_DUTY'
//   ];

//   RESPONDER_TYPES = [
//     'PARAMEDIC', 'FIREFIGHTER', 'POLICE_OFFICER',
//     'HAZMAT_SPECIALIST', 'RESCUE_TEAM'
//   ];

//   ngOnInit() {
//     if (this.responder) {
//       this.name = this.responder.name;
//       this.stationLocation = this.responder.stationLocation;
//       this.status = this.responder.status;
//       this.type = this.responder.type;
//       this.contact = this.responder.contact;
//       this.yearsOfExperience = this.responder.yearsOfExperience;
//     }
//   }

//   handleSubmit() {
//     const updatedResponder = {
//       id: this.responder?.id || Date.now(),
//       name: this.name,
//       stationLocation: this.stationLocation,
//       status: this.status,
//       type: this.type,
//       contact: this.contact,
//       yearsOfExperience: this.yearsOfExperience
//     };
//     this.submit.emit(updatedResponder);
//     this.dialogRef.close(); // Close the dialog after submission
//   }

//   cancel() {
//     this.dialogRef.close(); // Close the dialog on cancel
//   }
  
// }


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
  responderId: number = 1;
  name: string = '';
  stationLocation: string = '';
  status: ResponderStatus = ResponderStatus.AVAILABLE; // Default status is 'AVAILABLE'
  type: ResponderType = ResponderType.PARAMEDIC;
  locationId: number | null = null;

  RESPONDER_STATUSES = Object.values(ResponderStatus);
  RESPONDER_TYPES = Object.values(ResponderType);
RESPONDER_STATUS: any;

  constructor(
    private dialogRef: MatDialogRef<ResponderFormComponent>,
    private responderService: ResponderService
  ) {}

  ngOnInit() {
    if (this.responder) {
      this.name = this.responder.name;
      this.stationLocation = this.responder.stationLocation;
      this.status = this.responder.status;
      this.type = this.responder.type;
      this.locationId = this.responder.locationId || null;
    }
  }

  handleSubmit() {
    const responderData: ResponderDTO = {
      responderId: this.responderId,
      name: this.name,
      stationLocation: this.stationLocation,
      status: ResponderStatus.AVAILABLE,
      type: this.type,
      lastUpdate: new Date(),
      locationId: this.locationId || undefined,
      incidentId: 0
    };

    if (this.responder && this.responder.responderId) {
      // Updating existing responder
      this.responderService.updateResponder(this.responder.responderId, responderData)
        .subscribe({
          next: (updatedResponder) => {
            this.dialogRef.close(updatedResponder);
          },
          error: (error) => {
            console.error('Error updating responder', error);
          }
        });
    } else {
      // Creating new responder
      this.responderService.createResponder(responderData)
        .subscribe({
          next: (newResponder) => {
            this.dialogRef.close(newResponder);
          },
          error: (error) => {
            console.error('Error creating responder', error);
          }
        });
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}