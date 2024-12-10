
// import { Component } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { ResponderDTO, ResponderService } from '../../../core/services/responder.service';
// @Component({
//   selector: 'app-profile-header',
//   standalone: true,
//   imports: [CommonModule],
//   templateUrl: './profile-card.component.html',
//   styleUrls: ['./profile-card.component.css']
// })
// export class ProfileHeaderComponent {
//   responder!: ResponderDTO;
  
  
//   constructor(private responderService: ResponderService) {
//     console.log(this.responder.responderId);
//     // this.responderService.getResponderById(1).subscribe({
//     //   next: (responder) => {
//     //     this.responder = responder;
//     //   },
//     //   error: (error) => {
//     //     console.error('Error fetching responder', error);
//     //   }
//     // });
//   }
//   // status: 'available' | 'busy' | 'offline' = 'available';

//   // setStatus(newStatus: 'available' | 'busy' | 'offline') {
//   //   this.status = newStatus;
//   // }
// }

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponderDTO, ResponderService } from '../../../core/services/responder.service';

@Component({
  selector: 'app-profile-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.css']
})
export class ProfileHeaderComponent implements OnInit {
  responder!: ResponderDTO;
  
  constructor(private responderService: ResponderService) {}

  ngOnInit(): void {
    // Call the service method to fetch the responder data dynamically
    this.fetchResponderData();
  }

  fetchResponderData(): void {
    this.responderService.getResponderById(1).subscribe({
      
      next: (responder) => {
        this.responder = responder;
        console.log('Responder data fetched:', this.responder);
      },
      error: (error) => {
        console.error('Error fetching responder data:', error);
      }
    });
  }
}

