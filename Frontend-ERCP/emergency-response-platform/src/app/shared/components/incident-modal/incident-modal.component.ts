import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { finalize } from 'rxjs';

import { LocationService } from '../../../core/services/location.service';
import { EmergencyService } from '../../../core/services/emergency.service';
import { Incident, IncidentType } from '../../models/incident.model';

@Component({
  selector: 'app-incident-report-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatButtonModule],
  templateUrl: './incident-modal.component.html',
  styleUrls: ['./incident-modal.component.css']
})
export class IncidentReportModalComponent implements OnInit {
  readonly types = Object.values(IncidentType).filter(type => type !== IncidentType.SOS_REQUEST);
  readonly incidentForm;
  receipt?: Incident;
  locationPending = false;
  locationMessage = '';
  submissionError = '';
  submissionPending = false;

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private emergencyService: EmergencyService,
    public dialogRef: MatDialogRef<IncidentReportModalComponent>
  ) {
    this.incidentForm = this.createForm();
  }

  ngOnInit(): void {
    void this.fillLocation();
  }

  private createForm() {
    return this.fb.group({
      type: this.fb.nonNullable.control<IncidentType | ''>('', Validators.required),
      latitude: this.fb.control<number | null>(null, [Validators.required, Validators.min(-90), Validators.max(90)]),
      longitude: this.fb.control<number | null>(null, [Validators.required, Validators.min(-180), Validators.max(180)]),
      address: this.fb.nonNullable.control('', Validators.maxLength(255)),
      victimName: this.fb.nonNullable.control('', Validators.maxLength(255)),
      victimContact: this.fb.nonNullable.control('', Validators.maxLength(255))
    });
  }

  async fillLocation(force = false): Promise<void> {
    if (this.locationPending || this.submissionPending) return;
    this.locationPending = true;
    this.locationMessage = '';
    const {latitude, longitude} = this.incidentForm.controls;
    const previous = [latitude.value, longitude.value];
    try {
      const result = await this.locationService.getCurrentLocation();
      const unchanged = latitude.value === previous[0] && longitude.value === previous[1];
      if (!this.submissionPending && !this.receipt && unchanged && (force || (!latitude.dirty && !longitude.dirty))) {
        this.incidentForm.patchValue({latitude: result.latitude, longitude: result.longitude});
        this.locationMessage = 'Location filled from this device. Check it before submitting.';
      }
    } catch {
      this.locationMessage = 'Device location is unavailable. Enter the incident coordinates manually.';
    } finally {
      this.locationPending = false;
    }
  }

  onSubmit(): void {
    if (this.submissionPending || this.receipt) return;
    this.incidentForm.markAllAsTouched();
    if (this.incidentForm.invalid) return;
    const {type, latitude, longitude, address, victimName, victimContact} = this.incidentForm.getRawValue();
    if (!type || latitude === null || longitude === null || !Number.isFinite(latitude) || !Number.isFinite(longitude)) return;

    this.submissionPending = true;
    this.dialogRef.disableClose = true;
    this.submissionError = '';
    this.incidentForm.disable();
    const location = {latitude, longitude, address: address.trim() || undefined};
    this.emergencyService.reportIncident({
      incidentId: 0, type, status: 'NEW', responderIds: [],
      incidentLocation: location, victimLocation: location,
      victimName: victimName.trim(), victimContact: victimContact.trim()
    }).pipe(finalize(() => {
      this.submissionPending = false;
      this.dialogRef.disableClose = false;
      if (!this.receipt) this.incidentForm.enable();
    })).subscribe({
      next: receipt => { this.receipt = receipt; },
      error: () => {
        this.submissionError = 'We could not confirm receipt. The report may have been saved. For urgent help in India, call 112.';
      }
    });
  }
}
