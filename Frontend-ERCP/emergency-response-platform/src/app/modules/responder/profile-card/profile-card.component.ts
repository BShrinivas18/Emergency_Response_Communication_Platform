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
        console.log('Responder data fetched:', this.responder);
        console.log('Responder ID:', this.responder.responderId);  // Direct access
        console.log('Responder Name:', this.responder.name);  // Direct access
        console.log('Station Location:', this.responder.stationLocation);  // Direct access
        console.log('Status:', this.responder.status);  // Direct access
        console.log('Responder Type:', this.responder.type);

        // Convert the responder's status to string and set
        this.status = this.responder.status;
      },
      error: (error) => {
        console.error('Error fetching responder data:', error);
      }
    });
  }

  setStatus(newStatus: string): void {
    // Map the string value to ResponderStatus enum
    switch (newStatus) {
      case 'AVAILABLE':
        this.status = 'AVAILABLE'; // If using string status
        break;
      case 'ASSIGNED':
        this.status = 'ASSIGNED';  // If using string status
        break;
      case 'OFF_DUTY':
        this.status = 'OFF_DUTY';  // If using string status
        break;
      default:
        console.error('Unknown status:', newStatus);
    }

    if (this.responder) {
      console.log('Updating responder status:', this.status);
      this.responder.status = newStatus as ResponderStatus;  // Ensure it maps correctly to the enum
    }
    // Optionally, update the responder status in the backend
    this.responderService.updateResponder(this.responder.responderId, this.responder).subscribe({
      next: (updatedResponder) => {
        console.log('Responder status updated:', updatedResponder.status);
      },
      error: (error) => {
        console.error('Error updating responder status:', error);
      }
    });
    
  }

  logout() {
    // Call the logout method from your AuthService to clear session or tokens
    this.authService.logout();
    // Navigate to the login page or homepage
    this.router.navigate(['/login']);
  }
}
