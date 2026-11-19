import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ResponderService, ResponderType } from '../../../core/services/responder.service';
import { Incident } from '../../../shared/models/incident.model';
import { IncidentManagementService } from '../../../core/services/incident-management.service';
import { finalize, of, switchMap, timeout } from 'rxjs';

@Component({
  selector: 'app-incident',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './active-incidents.component.html',
  styleUrls: ['./active-incidents.component.css']
})
export class IncidentComponent implements OnInit {
  private readonly responders = inject(ResponderService);
  private readonly incidents = inject(IncidentManagementService);
  readonly responderForm = inject(FormBuilder).nonNullable.group({
    responderType: ['', Validators.required]
  });
  readonly responderTypes = Object.values(ResponderType);

  incident: Incident | null = null;
  loading = false;
  loadError = '';
  requestPending = false;
  requestMessage = '';

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    const id = Number(sessionStorage.getItem('responderId'));
    if (!id || this.loading) return;

    this.loading = true;
    this.loadError = '';
    this.requestMessage = '';
    this.responders.getResponderById(id).pipe(
      switchMap(responder => responder.incidentId
        ? this.incidents.getIncidentById(responder.incidentId)
        : of(null)),
      timeout(15000),
      finalize(() => this.loading = false)
    ).subscribe({
      next: incident => this.incident = incident,
      error: () => {
        this.incident = null;
        this.loadError = 'Could not load the current assignment. Try refreshing.';
      }
    });
  }

  requestResponders(): void {
    if (!this.incident || this.responderForm.invalid || this.requestPending) return;
    const type = this.responderForm.getRawValue().responderType as ResponderType;
    this.requestPending = true;
    this.requestMessage = '';
    this.responders.requestAdditionalResponders(this.incident.incidentId, type)
      .pipe(timeout(15000), finalize(() => this.requestPending = false))
      .subscribe({
        next: () => this.requestMessage = 'Request processed. Additional assignment has yet to be confirmed here.',
        error: () => this.requestMessage = 'Could not confirm the request. Check with your dispatcher before retrying.'
      });
  }
}
