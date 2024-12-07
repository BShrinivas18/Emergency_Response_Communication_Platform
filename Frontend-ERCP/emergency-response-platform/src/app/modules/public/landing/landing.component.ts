import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GoogleMap, MapMarker, GoogleMapsModule } from '@angular/google-maps';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { IncidentReportModalComponent } from '../../../shared/components/incident-modal/incident-modal.component';
import { LocationService } from '../../../core/services/location.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { WeatherService, WeatherAlert } from '../../../core/services/weather.service';
import { IncidentType, IncidentStatus } from '../../../shared/models/incident.model';
import { SOSAlertConfirmationComponent } from '../../../shared/components/sos-alert-modal/sos-alert-modal.component';
// import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  mapCenter = signal<google.maps.LatLngLiteral>({
    lat: 20.5937,
    lng: 78.9629
  });

  mapMarkers = signal<{position: google.maps.LatLngLiteral, title: string}[]>([]);
  weatherAlerts = signal<WeatherAlert[]>([]);
  
  emergencyTypes = ['Fire', 'Medical', 'Accident', 'Crime', 'Natural Disaster'];
  selectedEmergency!: string;
  
  guidelines: { [key: string]: string } = {
    'Fire': 'Evacuate immediately. Call emergency services. Use a fire extinguisher only if safe.',
    'Medical': 'Check for responsiveness. Call emergency services. Provide first aid if trained.',
    'Accident': 'Ensure scene is safe. Call emergency services. Do not move injured persons.',
    'Crime': 'Ensure personal safety. Call local law enforcement. Do not confront perpetrators.',
    'Natural Disaster': 'Follow local emergency guidelines. Stay informed. Prepare an emergency kit.'
  };

  constructor(
    private dialog: MatDialog,
    private locationService: LocationService,
    private emergencyService: EmergencyService,
    private weatherService: WeatherService
  ) {}

  ngOnInit() {
    this.initializeLocationAndAlerts();
  }

  private initializeLocationAndAlerts() {
    // Check if location permission has been previously granted
    const locationPermissionGranted = localStorage.getItem('locationPermissionGranted');
    
    if (!locationPermissionGranted) {
      this.locationService.requestLocationPermission().then(permissionGranted => {
        if (permissionGranted) {
          localStorage.setItem('locationPermissionGranted', 'true');
          this.getCurrentLocation();
        }
      });
    } else {
      this.getCurrentLocation();
    }
  }

  
  private requestLocationPermission() {
  
    this.locationService.requestLocationPermission().then(permissionGranted => {
      if (permissionGranted) {
        localStorage.setItem('locationPermissionGranted', 'true');
        this.getCurrentLocation();
      }
    });
  }

  getCurrentLocation() {
    this.locationService.getCurrentLocation()
      .then(location => {
        this.mapCenter.set({
          lat: location.latitude,
          lng: location.longitude
        });
        this.fetchWeatherAlerts();
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

  selectEmergency(emergency: string) {
    this.selectedEmergency = emergency;
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

  sendSOSAlert() {
    const currentLocation = this.mapCenter();
    this.emergencyService.sendSOSAlert({
      victimName: 'Anonymous',
      victimContact: '',
      location: {
        latitude: currentLocation.lat,
        longitude: currentLocation.lng
      },
      incidentType: IncidentType.MEDICAL,
      status: IncidentStatus.REPORTED
    }).subscribe({
      next: () => {
        this.dialog.open(SOSAlertConfirmationComponent, {
          data: {
            location: `Lat: ${currentLocation.lat}, Lng: ${currentLocation.lng}`
          }
        });
      },
      error: () => {
        console.error('SOS Alert failed');
      }
    });
  }

  getAlertSeverityClass(severity: 'low' | 'medium' | 'high'): string {
    return `${severity}-severity`;
  }
}