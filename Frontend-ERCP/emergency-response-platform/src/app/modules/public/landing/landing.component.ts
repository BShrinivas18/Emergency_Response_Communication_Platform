
// src/app/features/landing/landing.component.ts
import { Component, OnInit, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GoogleMap, MapMarker, GoogleMapsModule } from '@angular/google-maps';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

import { IncidentReportModalComponent } from '../../../shared/components/incident-modal/incident-modal.component';
import { LocationService } from '../../../core/services/location.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { WeatherService, WeatherAlert } from '../../../core/services/weather.service';
import { IncidentType, IncidentStatus, Incident } from '../../../shared/models/incident.model';
@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    GoogleMapsModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
    MatInputModule,
    MatFormFieldModule
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  @ViewChild(GoogleMap) map!: GoogleMap;

  mapCenter = signal<google.maps.LatLngLiteral>({
    lat: 20.5937,
    lng: 78.9629
  });

  mapMarkers = signal<{position: google.maps.LatLngLiteral, title: string}[]>([
    // { 
    //   position: { lat: 19.0760, lng: 72.8777 }, 
    //   title: 'Mumbai - Flood Warning' 
    // }
  ]);

  weatherAlerts = signal<WeatherAlert[]>([]);
  emergencyTypes = ['Fire', 'Medical', 'Accident', 'Crime', 'Natural Disaster'];
 
  selectedEmergency!: string;
  guidelines: { [key: string]: string } = {
    'Fire': 'Evacuate the building immediately. Call the fire department...',
    'Medical': 'Call 911 immediately for any medical emergency...',
    'Accident': 'Ensure safety first. Call the police...',
    'Crime': 'Stay safe. Call the police...',
    'Natural Disaster': 'Seek shelter immediately. Stay informed...'
  };

  constructor(
    private dialog: MatDialog,
    private locationService: LocationService,
    private emergencyService: EmergencyService,
    private weatherService: WeatherService
  ) {}

  ngOnInit() {
    this.getCurrentLocation();
    this.fetchWeatherAlerts();
  }

  selectEmergency(emergency: string) {
    this.selectedEmergency = emergency;
  }

  getCurrentLocation() {
    this.locationService.getCurrentLocation()
      .then(location => {
        this.mapCenter.set({
          lat: location.latitude,
          lng: location.longitude
        });
      })
      .catch(error => {
        console.error('Location retrieval failed', error);
      });
  }

  fetchWeatherAlerts() {
    const { lat, lng } = this.mapCenter();
    this.weatherService.getWeatherAlerts(lat, lng)
      .subscribe({
        next: (alerts) => {
          this.weatherAlerts.set(alerts);
        },
        error: (err) => {
          console.error('Weather alerts fetch failed', err);
        }
      });
  }

  openIncidentReportModal() {
    const dialogRef = this.dialog.open(IncidentReportModalComponent, {
      width: '500px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Handle incident report submission
        console.log('Incident reported:', result);
      }
    });
  }

  // sendSOSAlert() {
  //   const currentLocation = this.mapCenter();
  //   this.emergencyService.sendSOSAlert({
  //     victimName: 'Anonymous',
  //     victimContact: '',
  //     location: {
  //       latitude: currentLocation.lat,
  //       longitude: currentLocation.lng
  //     },
  //     incidentType: IncidentType.MEDICAL,
  //     status: IncidentStatus.REPORTED
  //   }).subscribe({
  //     next: () => {
  //       // Show SOS alert confirmation
  //       console.log('SOS Alert sent');
  //     },
  //     error: () => {
  //       console.error('SOS Alert failed');
  //     }
  //   });
  // }
  sendSOSAlert() {
    console.log("SOS Alert Sent!");
    alert("SOS Alert Sent!");
    // Your SOS alert logic here
  }
}
