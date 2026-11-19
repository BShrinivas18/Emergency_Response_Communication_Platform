import { Component } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { firstValueFrom } from 'rxjs';

import { IncidentReportModalComponent } from '../../../shared/components/incident-modal/incident-modal.component';
import { LocationService } from '../../../core/services/location.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { IncidentType } from '../../../shared/models/incident.model';
import { SOSAlertConfirmationComponent } from '../../../shared/components/sos-alert-modal/sos-alert-modal.component';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, RouterLink],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  submissionPending = false;
  submissionError = '';

  constructor(
    private dialog: MatDialog,
    private locationService: LocationService,
    private emergencyService: EmergencyService
  ) {}

  openIncidentReportModal(): void {
    this.dialog.open(IncidentReportModalComponent, {width: '560px', maxWidth: '94vw'});
  }

  async sendSOSAlert(): Promise<void> {
    if (this.submissionPending) return;
    this.submissionPending = true;
    this.submissionError = '';
    try {
      const location = await this.locationService.getCurrentLocation();
      const coordinates = {latitude: location.latitude, longitude: location.longitude};
      try {
        const receipt = await firstValueFrom(this.emergencyService.reportIncident({
          incidentId: 0, type: IncidentType.SOS_REQUEST, status: 'NEW', responderIds: [],
          incidentLocation: coordinates, victimLocation: coordinates, victimName: '', victimContact: ''
        }));
        this.dialog.open(SOSAlertConfirmationComponent, {data: {incident: receipt}});
      } catch {
        this.submissionError = 'SOS receipt was not confirmed. For urgent help in India, call 112.';
      }
    } catch {
      this.submissionError = 'Device location is unavailable, so no report sent. Enter the location in Report an incident or call 112 in India.';
    } finally {
      this.submissionPending = false;
    }
  }
}
