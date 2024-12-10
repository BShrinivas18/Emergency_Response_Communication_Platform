

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
import { timestamp } from 'rxjs';
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
  type: string[] = ['Fire', 'Medical Emergency', 'Crime', 'Natural Disaster', 'Accident','Chemical/Hazmat'];
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
      type: ['', Validators.required],
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
        incidentlocation: this.userLocation ? {
          latitude: this.userLocation.lat,
          longitude: this.userLocation.lng,
          address: this.incidentForm.value.incidentLocation
        } : null,
        victimlocation: this.userLocation ? {
          latitude: this.userLocation.lat,
          longitude: this.userLocation.lng,
          address: this.incidentForm.value.incidentLocation
        } : null,
        status: 'NEW',
        timestamp: new Date(),
      };
      console.log("incoming data from form : ")
      console.log(incidentData)
      this.emergencyService.reportIncident(incidentData).subscribe((response) => {
        console.log("sent request")
        console.log(response)
      });
      this.emergencyService.setFormData(incidentData);
      this.emergencyService.openIncidentConfirmationModal(incidentData);
      this.dialogRef.close();
    }
  }
}
