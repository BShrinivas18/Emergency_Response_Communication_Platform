import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { API_ORIGIN } from '../../../core/api.config';
@Component({selector: 'app-incident-history', standalone: true, imports: [CommonModule],
 templateUrl: './past-incidents.component.html', styleUrls: ['./past-incidents.component.css']})
export class IncidentHistoryComponent implements OnInit {
  events: {incidentId: number | null; eventType: string; previousStatus: string; newStatus: string; occurredAt: string}[] = [];
  message = 'Loading history...';
  constructor(private http: HttpClient) {}
  ngOnInit() {
    const id = Number(sessionStorage.getItem('responderId'));
    if (!Number.isInteger(id) || id <= 0) { this.message = 'Responder identity unavailable.'; return; }
    this.http.get<typeof this.events>(`${API_ORIGIN}/responders/${id}/history`).subscribe({
      next: events => { this.events = events; this.message = events.length ? '' : 'No recorded history since auditing was enabled.'; },
      error: () => this.message = 'History could not be loaded.'
    });
  }
}
