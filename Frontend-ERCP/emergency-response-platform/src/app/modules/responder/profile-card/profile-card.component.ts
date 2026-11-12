import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponderDTO, ResponderService, ResponderStatus } from '../../../core/services/responder.service';  // Ensure correct import
import {  Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
@Component({
  selector: 'app-profile-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileHeaderComponent implements OnInit {
  responder: ResponderDTO = {} as ResponderDTO;
  status!: string;  // Use string type to store the status temporarily

  constructor(private responderService: ResponderService, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Call the service method to fetch the responder data dynamically
    this.fetchResponderData();
  }

  fetchResponderData(): void {
    this.responderService.getResponderById(Number(sessionStorage.getItem('responderId'))).subscribe({
      next: (responder) => {
        this.responder = responder;





        // Convert the responder's status to string and set
        this.status = this.responder.status;
      },
      error: (error) => {
        console.error('Error fetching responder data:', error);
      }
    });
  }

  statusError = '';
  statusPending = false;
  setStatus(newStatus: string): void {
    if (this.statusPending || !['AVAILABLE', 'OFF_DUTY'].includes(newStatus)) return;
    this.statusPending = true; this.statusError = '';
    this.responderService.updateStatus(this.responder.responderId, newStatus as ResponderStatus).subscribe({
      next: updated => { this.responder = updated; this.status = updated.status; this.statusPending = false; },
      error: () => { this.statusError = 'Status update not confirmed.'; this.statusPending = false; }
    });
  }

  logout() {
    // Call the logout method from your AuthService to clear session or tokens
    this.authService.logout();
    // Navigate to the login page or homepage
    this.router.navigate(['/login']);
  }
}
