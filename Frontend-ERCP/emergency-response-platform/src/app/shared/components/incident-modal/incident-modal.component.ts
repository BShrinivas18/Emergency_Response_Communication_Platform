
// src/app/features/incident-report-modal/incident-report-modal.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { IncidentType } from '../../../shared/models/incident.model';
import { LocationService } from '../../../core/services/location.service';

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
    MatDialogModule
  ], 
  templateUrl: './incident-modal.component.html',
  styleUrls: ['./incident-modal.component.css']
})
export class IncidentReportModalComponent implements OnInit {
  
  incidentForm! : FormGroup;
  incidentTypes = ['Fire', 'Medical Emergency', 'Crime', 'Natural Disaster'];
  userLocation: { lat: number; lng: number } | null = null;

  // submittedIncident :any = null;
  
  // confirmedLocation: { lat: number; lng: number } | null = null;

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private dialogRef: MatDialogRef<IncidentReportModalComponent>
  ) {}

  ngOnInit() {
    this.initForm();
    this.getUserLocation();
  }

  initForm() {
    this.incidentForm = this.fb.group({
      // victimName: ['', Validators.required],
      // victimContact: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      // incidentType: ['', Validators.required],
      // location: ['', Validators.required]
      victimName: ['', Validators.required],
      victimContact: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      victimAddress: ['', Validators.required],
      incidentType: ['', Validators.required],
      incidentLocation: ['', Validators.required],
      useSameLocation: [false],
    });
  }
   
  getUserLocation() {
    this.locationService.getCurrentLocation().then((location) => {
      this.userLocation = { lat: location.latitude, lng: location.longitude };
      this.locationService
        .getAddressFromCoordinates(location.latitude, location.longitude)
        .then((address) => {
          this.incidentForm.patchValue({
            victimAddress: address,
          });
        });
    });
  }

  useCurrentLocation(field: 'victimAddress' | 'incidentLocation') {
    if (this.userLocation) {
      this.locationService
        .getAddressFromCoordinates(this.userLocation.lat, this.userLocation.lng)
        .then((address) => {
          this.incidentForm.patchValue({
            [field]: address,
          });
        });
    }
  }

  updateIncidentLocation() {
    if (this.incidentForm.value.useSameLocation) {
      this.incidentForm.patchValue({
        incidentLocation: this.incidentForm.value.victimAddress,
      });
    }
  }

  openMapModal() {
    // Open a map modal using Google Maps API
    const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
      center: this.userLocation || { lat: 0, lng: 0 },
      zoom: 15,
    });

    const marker = new google.maps.Marker({
      position: this.userLocation || { lat: 0, lng: 0 },
      map,
      draggable: true,
    });
  
    google.maps.event.addListener(marker, 'dragend', () => {
      const position = marker.getPosition();
      if (position) {
        const lat = position.lat();
        const lng = position.lng();
        this.locationService.getAddressFromCoordinates(lat, lng).then((address) => {
          this.incidentForm.patchValue({
            incidentLocation: address,
          });
        });
      }
    });
  }

  onSubmit() {
    if (this.incidentForm.valid) {
      console.log('Incident submitted:', this.incidentForm.value);

      // this.submittedIncident = this.incidentForm.value;
      this.dialogRef.close(this.incidentForm.value);
    }
  }
  viewIncidentProgress(){
    window.open('https://www.google.com', '_blank');
  }
}
