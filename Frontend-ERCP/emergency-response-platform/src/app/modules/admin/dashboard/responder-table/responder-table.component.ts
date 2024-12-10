// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { ResponderService, ResponderDTO } from '../../../../core/services/responder.service';

// @Component({
//   standalone: true,
//   selector: 'app-responder-table',
//   imports: [CommonModule],
//   templateUrl: './responder-table.component.html',
//   styleUrls: ['./responder-table.component.css']
// })
// export class ResponderTableComponent implements OnInit {
//   responders: ResponderDTO[] = [];

//   constructor(private responderService: ResponderService) {}

//   ngOnInit() {
//     this.responderService.getAvailableResponders().subscribe(
//       (data: ResponderDTO[]) => {
//         this.responders = data;
//       },
//       error => {
//         console.error('Error fetching responders', error);
//       }
//     );
//   }
// }



import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResponderService, ResponderDTO } from '../../../../core/services/responder.service'; // Import the ResponderService

@Component({
  standalone: true,
  selector: 'app-responder-table',
  imports: [CommonModule],
  templateUrl: './responder-table.component.html',
  styleUrls: ['./responder-table.component.css']
})
export class ResponderTableComponent implements OnInit {
  responders: ResponderDTO[] = [];

  constructor(private responderService: ResponderService) {}

  ngOnInit() {
    this.loadAvailableResponders();
  }

  loadAvailableResponders() {
    this.responderService.getAvailableResponders().subscribe({
      next: (data: ResponderDTO[]) => {
        this.responders = data;
        console.log('Available Responders:', this.responders);
      },
      error: (error) => {
        console.error('Error fetching responders:', error);
        // Handle error (e.g., show error message)
      }
    });
  }
}

