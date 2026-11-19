import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize, timeout } from 'rxjs';
import { ResponderDTO, ResponderService, ResponderStatus } from '../../../core/services/responder.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-profile-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileHeaderComponent implements OnInit {
  @Output() readonly statusChanged = new EventEmitter<void>();
  responder = {} as ResponderDTO;
  status = '';
  statusError = '';
  statusPending = false;

  constructor(private responderService: ResponderService, private authService: AuthService) {}

  ngOnInit(): void {
    this.fetchResponderData();
  }

  fetchResponderData(): void {
    const id = Number(sessionStorage.getItem('responderId'));
    if (!id || this.statusPending) return;

    this.statusPending = true;
    this.statusError = '';
    this.responderService.getResponderById(id).pipe(timeout(15000), finalize(() => this.statusPending = false)).subscribe({
      next: responder => {
        this.responder = responder;
        this.status = responder.status;
      },
      error: () => this.statusError = 'Could not load your responder profile.'
    });
  }

  setStatus(newStatus: string): void {
    if (!this.responder.responderId || this.statusPending ||
          !['AVAILABLE', 'OFF_DUTY', 'ON_ROUTE', 'ON_SCENE'].includes(newStatus)) return;
    this.statusPending = true;
    this.statusError = '';
    this.responderService.updateStatus(this.responder.responderId, newStatus as ResponderStatus)
      .pipe(timeout(15000), finalize(() => this.statusPending = false))
      .subscribe({
        next: updated => {
          this.responder = updated;
          this.status = updated.status;
          this.statusChanged.emit();
        },
        error: () => this.statusError = 'Status update not confirmed. Refresh your dashboard before retrying.'
      });
  }

  logout(): void {
    this.authService.logout();
  }
}
