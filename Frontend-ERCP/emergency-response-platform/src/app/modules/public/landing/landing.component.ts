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
  selectedEmergency!: string;
  
  guidelines: { [key: string]: string[] } = {
    'Fire': [
      'Evacuate immediately:',
      '- Leave the building as quickly as possible.',
      '- Use stairs, not elevators.',
      '- Follow the designated escape routes.',
      'Report and incident into our system, help will be on the way ASAP',
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
      'Report and incident into our system, help will be on the way ASAP',
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
      'Report and incident into our system, help will be on the way ASAP',
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
        console.log('Incident reported:', result);
      }
    });
  }

  async sendSOSAlert() {
   
    try {
      // Wait for the address to resolve (this is your ZoneAwarePromise)
      const currentLocation = await this.locationService.getCurrentLocation();
  
      // Resolve the promise and get the address
      const address = currentLocation.formattedAddress;
  
      // Log the formatted address
      console.log("Formatted Address:", address);

       this.incident = {
        incidentLocation:  {
          latitude: currentLocation.latitude,
          longitude: currentLocation.longitude,
          address: address, // Set the resolved address here
        } ,
        victimLocation:{
          latitude: currentLocation.latitude,
          longitude: currentLocation.longitude,
          address: address, // Set the resolved address here
        },
        timestamp: new Date(),
        victimName: 'Anonymous',
        victimContact: '',
        status: 'NEW',
        type: IncidentType.SOS_REQUEST,
        responderIds:[],
        incidentId:0,
      } 
      // this.emergencyService.reportIncident(incident);
      // Proceed with the dialog opening using the resolved address
      this.dialog.open(SOSAlertConfirmationComponent, {
        data: {
          incident: {
            victimName: 'Anonymous',
            victimContact: '',
            location: {
              latitude: currentLocation.latitude,
              longitude: currentLocation.longitude,
              address: address, // Set the resolved address here
            },
            status: 'Reported',
            incidentType: IncidentType.SOS_REQUEST,
            timestamp: new Date(),
          },
        },
      });

      this.emergencyService.reportIncident(this.incident).subscribe({
        next: (response) => {
          console.log("Sent SOS Alert");
          console.log(response);
        },
        error: (error) => {
          console.error("Error sending SOS Alert:", error);
        }
      });
    } catch (error) {
      console.error("Error retrieving address:", error);
    }
  
     
  }

  getAlertSeverityClass(severity: 'low' | 'medium' | 'high'): string {
    return `${severity}-severity`;
  }
  onLogin() {
    this.router.navigate(['/login']);
  }
}