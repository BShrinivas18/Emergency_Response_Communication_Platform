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
import { IncidentType, IncidentStatus,Incident } from '../../../shared/models/incident.model';
import { SOSAlertConfirmationComponent } from '../../../shared/components/sos-alert-modal/sos-alert-modal.component';
// import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Router } from '@angular/router';
import { WarningDialogComponent } from './warning-dialog/warning-dialog.component';
import { DialogModule, DialogRef } from '@angular/cdk/dialog';
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
    RouterModule
  ],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {
  incident!:Incident;
  mapCenter = signal<google.maps.LatLngLiteral>({
    lat: 20.5937,
    lng: 78.9629
  });

  mapMarkers = signal<{position: google.maps.LatLngLiteral, title: string}[]>([]);
  weatherAlerts = signal<WeatherAlert[]>([]);
  
  emergencyTypes = ['Fire', 'Medical', 'Accident', 'Crime', 'Natural Disaster'];
  // selectedEmergency!: string;
  selectedEmergency: string | null = null;
  
  guidelines: { [key: string]: string[] } = {
    'Fire': [
      'Evacuate immediately:',
      '- Leave the building as quickly as possible.',
      '- Use stairs, not elevators.',
      '- Follow the designated escape routes.',
      'A report does not confirm responder assignment. For urgent help use official emergency calling.',
      'Use a fire extinguisher only if safe:',
      '- Only attempt to extinguish the fire if it\'s small and manageable.',
      '- Stand with your back to an exit in case you need to escape.',
      '- Use the P.A.S.S. method: Pull the pin, Aim at the base of the fire, Squeeze the handle, Sweep side to side.',
      'Do not re-enter the building:',
      '- Once you have evacuated, do not go back inside for any reason.'
    ],
    'Medical': [
      'Check for responsiveness:',
      '- Gently tap the person and shout to see if they respond.',
      '- If there is no response, check for breathing and a pulse.',
      'A report does not confirm responder assignment. For urgent help use official emergency calling.',
      'Provide first aid if trained:',
      '- CPR: If the person is not breathing, begin CPR if you\'re trained.',
      '- Control bleeding: Apply pressure to wounds with a clean cloth to stop bleeding.',
      '- Treat for shock: Lay the person down, elevate their legs, and keep them warm.',
      'Do not move the person:',
      '- Only move the person if they are in immediate danger (e.g., fire or collapse).'
    ],
    'Accident': [
      'Ensure scene is safe:',
      '- Check the surroundings for dangers such as fires, electrical hazards, or other vehicles.',
      '- Move to a safe location if necessary.',
      'Do not move injured persons:',
      '- Only move injured individuals if their life is in immediate danger (e.g., from a fire or explosion).',
      '- Avoid causing further injury by moving someone with possible spinal injuries.',
      'Provide assistance if trained:',
      '- If trained in first aid, check for bleeding, broken bones, or signs of shock.',
      '- Offer assistance, but do not interfere with emergency responders.'
    ],
    'Crime': [
      'Ensure personal safety:',
      '- Avoid engaging or confronting the perpetrator.',
      '- Keep a safe distance from the scene.',
      'A report does not confirm responder assignment. For urgent help use official emergency calling.',
      '- Give detailed information about the crime, location, and description of the suspect(s).',
      'Do not confront perpetrators:',
      '- Do not attempt to stop the perpetrator or interfere with their actions.',
      '- Remain as calm as possible and provide information to law enforcement.',
      'Preserve evidence:',
      '- Avoid touching or disturbing evidence at the scene.',
      '- If safe to do so, document details like the suspect’s appearance, vehicle, and direction of escape.'
    ],
    'Natural Disaster': [
      'Follow local emergency guidelines:',
      '- Listen to local authorities for evacuation orders or safety recommendations.',
      '- Follow evacuation routes and avoid areas that are at high risk.',
      'Stay informed:',
      '- Stay tuned to weather reports, news updates, or emergency alerts via radio, TV, or mobile apps.',
      '- Be aware of changing conditions (e.g., floods, wildfires, tornadoes).',
      'Prepare an emergency kit:',
      '- Pack essentials like water, non-perishable food, flashlights, first aid supplies, medications, and important documents.',
      '- Ensure your emergency kit is easily accessible and ready to go at a moment\'s notice.',
      'Stay indoors during hazardous conditions:',
      '- Avoid traveling during severe weather (e.g., during a hurricane or tornado).',
      '- Stay inside safe structures and away from windows and doors.'
    ]
  };
  
  

  constructor(
    private dialog: MatDialog,
    private locationService: LocationService,
    private emergencyService: EmergencyService,
    private weatherService: WeatherService,
    private router: Router
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

      }
    });
  }

  submissionPending = false;
  submissionError = '';
  async sendSOSAlert() {
    if (this.submissionPending) return;
    this.submissionPending = true;
    this.submissionError = '';
    try {
      const location = await this.locationService.getCurrentLocation();
      const coordinates = { latitude: location.latitude, longitude: location.longitude, address: location.formattedAddress };
      this.incident = {incidentId: 0, type: IncidentType.SOS_REQUEST, status: 'NEW',
        incidentLocation: coordinates, victimLocation: coordinates, victimName: '', victimContact: '', responderIds: []};
      this.emergencyService.reportIncident(this.incident).subscribe({
        next: receipt => {
          this.submissionPending = false;
          this.dialog.open(SOSAlertConfirmationComponent, {data: {incident: receipt}});
        },
        error: () => {
          this.submissionPending = false;
          this.submissionError = 'Receipt not confirmed. Use official emergency calling where appropriate (India: 112).';
        }
      });
    } catch {
      this.submissionPending = false;
      this.submissionError = 'Unable to locate you; no report sent. Use official emergency calling (India: 112).';
    }
  }

  getAlertSeverityClass(severity: 'low' | 'medium' | 'high'): string {
    return `${severity}-severity`;
  }
  onLogin() {
    this.router.navigate(['/login']);
  }
}
