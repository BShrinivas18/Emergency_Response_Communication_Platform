
// // src/app/features/incident-report-modal/incident-report-modal.component.ts
// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { MatDialogRef } from '@angular/material/dialog';
// import { MatDialogModule } from '@angular/material/dialog';
// import { MatButtonModule } from '@angular/material/button';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatCardModule } from '@angular/material/card';
// import { IncidentType } from '../../../shared/models/incident.model';
// import { LocationService } from '../../../core/services/location.service';

// @Component({
//   selector: 'app-incident-report-modal',
//   standalone: true,
//   imports: [
//     CommonModule,
//     FormsModule,
//     ReactiveFormsModule,
//     MatButtonModule,
//     MatInputModule,
//     MatSelectModule,
//     MatFormFieldModule,
//     MatCardModule,
//     MatDialogModule,
//   ], 
//   templateUrl: './incident-modal.component.html',
//   styleUrls: ['./incident-modal.component.css']
// })
// export class IncidentReportModalComponent implements OnInit {
  
//   incidentForm! : FormGroup;
//   incidentTypes:string[] = ['Fire', 'Medical Emergency', 'Crime', 'Natural Disaster','Accident'];
//   userLocation: { lat: number; lng: number } | null = null;

//   // submittedIncident :any = null;
  
//   // confirmedLocation: { lat: number; lng: number } | null = null;

//   constructor(
//     private fb: FormBuilder,
//     private locationService: LocationService,
//     private dialogRef: MatDialogRef<IncidentReportModalComponent>
//   ) {
//     this.incidentForm = this.fb.group({
//       incidentType: [''], // Default value
//     });
//   }

//   ngOnInit() {
//     this.initForm();
//     this.getUserLocation();
//   }

//   initForm() {
//     this.incidentForm = this.fb.group({
//       // victimName: ['', Validators.required],
//       // victimContact: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
//       // incidentType: ['', Validators.required],
//       // location: ['', Validators.required]
//       victimName: ['', Validators.required],
//       victimContact: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
//       victimAddress: ['', Validators.required],
//       incidentType: ['', Validators.required],
//       incidentLocation: ['', Validators.required],
//       useSameLocation: [false],
//     });
//   }
   
//   getUserLocation() {
//     this.locationService.getCurrentLocation().then((location) => {
//       this.userLocation = { lat: location.latitude, lng: location.longitude };
//       this.locationService
//         .getAddressFromCoordinates(location.latitude, location.longitude)
//         .then((address:any) => {
//           this.incidentForm.patchValue({
//             victimAddress: address,
//           });
//         });
//     });
//   }

//   useCurrentLocation(field: 'victimAddress' | 'incidentLocation') {
//     if (this.userLocation) {
//       this.locationService
//         .getAddressFromCoordinates(this.userLocation.lat, this.userLocation.lng)
//         .then((address:any) => {
//           this.incidentForm.patchValue({
//             [field]: address,
//           });
//         });
//     }
//   }

//   updateIncidentLocation() {
//     if (this.incidentForm.value.useSameLocation) {
//       this.incidentForm.patchValue({
//         incidentLocation: this.incidentForm.value.victimAddress,
//       });
//     }
//   }

//   openMapModal() {
//     // Open a map modal using Google Maps API
//     const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
//       center: this.userLocation || { lat: 0, lng: 0 },
//       zoom: 15,
//     });

//     const marker = new google.maps.Marker({
//       position: this.userLocation || { lat: 0, lng: 0 },
//       map,
//       draggable: true,
//     });
  
//     google.maps.event.addListener(marker, 'dragend', () => {
//       const position = marker.getPosition();
//       if (position) {
//         const lat = position.lat();
//         const lng = position.lng();
//         this.locationService.getAddressFromCoordinates(lat, lng).then((address:any) => {
//           this.incidentForm.patchValue({
//             incidentLocation: address,
//           });
//         });
//       }
//     });
//   }

  // onSubmit() {
//     if (this.incidentForm.valid) {
//       console.log('Incident submitted:', this.incidentForm.value);

//       // this.submittedIncident = this.incidentForm.value;
//       this.dialogRef.close(this.incidentForm.value);
//     }
//   }
//   viewIncidentProgress(){
//     window.open('https://www.google.com', '_blank');
//   }

// }




// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
// import { MatButtonModule } from '@angular/material/button';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatCardModule } from '@angular/material/card';
// import { MatCheckboxModule } from '@angular/material/checkbox';
// import { MatIconModule } from '@angular/material/icon';

// import { LocationService } from '../../../core/services/location.service';
// import { EmergencyService } from '../../../core/services/emergency.service';
// import { IncidentType, IncidentStatus } from '../../../shared/models/incident.model';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// // import { IncidentConfirmationComponent } from '../incident-confirmation-modal/incident-confirmation.component';

// @Component({
//   selector: 'app-incident-report-modal',
//   standalone: true,
//   imports: [
//     CommonModule,
//     FormsModule,
//     ReactiveFormsModule,
//     MatButtonModule,
//     MatInputModule,
//     MatSelectModule,
//     MatFormFieldModule,
//     MatCardModule,
//     MatDialogModule,
//     MatCheckboxModule,
//     MatIconModule,
//   ], 
//   templateUrl: './incident-modal.component.html',
//   styleUrls: ['./incident-modal.component.css']
// })
// export class IncidentReportModalComponent implements OnInit {
  
//   incidentForm!: FormGroup;
//   // incidentTypes = Object.values(IncidentType);
//   incidentTypes: string[] = ['Fire', 'Medical Emergency', 'Crime', 'Natural Disaster','Accident'];
//   userLocation: { lat: number; lng: number } | null = null;
//   usesamelocation: boolean = false;
//   constructor(
//     private fb: FormBuilder,
//     private locationService: LocationService,
//     private emergencyService: EmergencyService,
//     private dialogRef: MatDialogRef<IncidentReportModalComponent>
//   ) {
//   }

//   ngOnInit() {
//     this.initForm();
//     this.getUserLocation();
//   }

//   initForm() {
//     this.incidentForm = this.fb.group({
//       victimName: [''],
//       victimContact: ['', [Validators.pattern(/^\d{10}$/)]],
//       victimAddress: ['', Validators.required],
//       // incidentType: ['', Validators.required],
//       incidentLocation: ['']
//       // useSameLocation: [false]
//     });
//   }
   
//   getUserLocation() {
//     this.locationService.getCurrentLocation().then((location) => {
//       this.userLocation = { 
//         lat: location.latitude, 
//         lng: location.longitude 
//       };
      
//       // Automatically fill victim address if available
//       if (location.formattedAddress) {
//         this.incidentForm.patchValue({
//           victimAddress: location.formattedAddress,
//           incidentLocation: location.formattedAddress
//         });
//       }
//     }).catch(error => {
//       console.error('Could not get location', error);
//     });
//   }

//   useCurrentLocation(field: 'victimAddress' | 'incidentLocation') {
//     if (this.userLocation) {
//       this.locationService
//         .getAddressFromCoordinates(this.userLocation.lat, this.userLocation.lng)
//         .then((addressDetails) => {
//           this.incidentForm.patchValue({
//             [field]: addressDetails.formattedAddress || ''
//           });
//         });
//     }
//   }

//   updateIncidentLocation() {
//     const useSameLocation = this.incidentForm.get('useSameLocation')?.value;

//     // If the checkbox is checked, set the incident location to the victim's address
//     if (useSameLocation) {
//       this.incidentForm.patchValue({
//         incidentLocation: this.incidentForm.get('victimAddress')?.value,
//       });
//     } else {
//       this.incidentForm.patchValue({
//         incidentLocation: '',
//       });
//     }
//   }

//   onSubmit() {
    

//     if (this.incidentForm.valid) {
//       const incidentData = {
//         ...this.incidentForm.value,
//         location: this.userLocation ? {
//           latitude: this.userLocation.lat,
//           longitude: this.userLocation.lng,
//           address: this.incidentForm.value.incidentLocation
//         } : null,
//         status: IncidentStatus.REPORTED
//       };

//       this.emergencyService.reportIncident(incidentData).subscribe({
//         next: (response) => {
//           this.dialogRef.close();
//           this.dialogRef.afterClosed().subscribe(() => {
//             // Open confirmation modal
//             // this.emergencyService.openIncidentConfirmationModal(response);
//           });
//         },
//         error: (error) => {
//           console.error('Incident report failed', error);
//           // Optionally show error to user
//         }
//       });
//       this.dialogRef.close();
//     }
//   }
// }










import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';

import { LocationService } from '../../../core/services/location.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { IncidentStatus } from '../../../shared/models/incident.model'; 
@Component({
  selector: 'app-incident-report-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatCardModule,
    MatDialogModule,
    MatCheckboxModule,
    MatIconModule,
  ], 
  templateUrl: './incident-modal.component.html',
  styleUrls: ['./incident-modal.component.css']
})
export class IncidentReportModalComponent implements OnInit {
  
  incidentForm!: FormGroup;
  incidentTypes: string[] = ['Fire', 'Medical Emergency', 'Crime', 'Natural Disaster', 'Accident'];
  userLocation: { lat: number; lng: number } | null = null;

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private emergencyService: EmergencyService,
    private dialogRef: MatDialogRef<IncidentReportModalComponent>
  ) {}

  ngOnInit() {
    this.initForm();
    this.getUserLocation();
  }

  initForm() {
    this.incidentForm = this.fb.group({
      victimName: ['', Validators.required],
      victimContact: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      victimAddress: ['', Validators.required],
      incidentTypes: ['', Validators.required],
      incidentLocation: [''],
      useSameLocation: [true],  // Add this field to the form
    });
  }

  getUserLocation() {
    this.locationService.getCurrentLocation().then((location) => {
      this.userLocation = { 
        lat: location.latitude, 
        lng: location.longitude 
      };
      
      if (location.formattedAddress) {
        this.incidentForm.patchValue({
          victimAddress: location.formattedAddress,
          incidentLocation: location.formattedAddress,
        });
      }
    }).catch(error => {
      console.error('Could not get location', error);
    });
  }

  useCurrentLocation(field: 'victimAddress' | 'incidentLocation') {
    if (this.userLocation) {
      this.locationService
        .getAddressFromCoordinates(this.userLocation.lat, this.userLocation.lng)
        .then((addressDetails) => {
          this.incidentForm.patchValue({
            [field]: addressDetails.formattedAddress || ''
          });
        });
    }
  }

  updateIncidentLocation() {
    const useSameLocation = this.incidentForm.get('useSameLocation')?.value;
    
    if (useSameLocation) {
      // Fill incidentLocation with victimAddress
      this.incidentForm.patchValue({
        incidentLocation: this.incidentForm.get('victimAddress')?.value,
      });
    } else {
      // Clear incidentLocation
      this.incidentForm.patchValue({
        incidentLocation: '',
      });
    }
  }

  onSubmit() {
    if (this.incidentForm.valid) {
      const incidentData = {
        ...this.incidentForm.value,
        location: this.userLocation ? {
          latitude: this.userLocation.lat,
          longitude: this.userLocation.lng,
          address: this.incidentForm.value.incidentLocation
        } : null,
        status: 'Reported',
        reportedAt: new Date(),

        
       
      };
      // console.log(incidentData);
      this.emergencyService.setFormData(incidentData);
      this.emergencyService.openIncidentConfirmationModal(incidentData);
      this.dialogRef.close();
    }
  }
}
