// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-responder-details',
//   imports: [],
//   templateUrl: './responder-details.component.html',
//   styleUrl: './responder-details.component.css'
// })
// export class ResponderDetailsComponent {

// }
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ResponderFormComponent } from '../responder-form/responder-form.component';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule } from '@angular/material/dialog';


@Component({
  standalone:true,
  imports:[ResponderFormComponent,MatCardModule,MatDialogModule],
  selector: 'app-responder-details',
  templateUrl: './responder-details.component.html',
  styleUrls: ['./responder-details.component.css']
})
export class ResponderDetailsComponent {
  @Input() responderId!: number;
  @Output() close = new EventEmitter<void>();

  responder: any = null;
  isEditing = false;

  ngOnInit() {
    // Mock Data; Replace with actual API call
    this.responder = {
      id: this.responderId,
      name: 'John Doe',
      type: 'FIREFIGHTER',
      status: 'AVAILABLE',
      stationLocation: 'Central Station',
      contact: 'john.doe@example.com',
      yearsOfExperience: 5
    };
  }

  closeDetails() {
    this.close.emit();
  }

  updateResponder(updatedResponder: any) {
    this.responder = updatedResponder;
    this.isEditing = false;
  }
}

