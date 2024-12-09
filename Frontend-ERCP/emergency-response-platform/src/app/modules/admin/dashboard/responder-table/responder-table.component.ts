// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-responder-table',
//   imports: [],
//   templateUrl: './responder-table.component.html',
//   styleUrl: './responder-table.component.css'
// })
// export class ResponderTableComponent {

// }

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  standalone:true,
  selector: 'app-responder-table',
  imports:[CommonModule],
  templateUrl: './responder-table.component.html',
  styleUrls: ['./responder-table.component.css']
})
export class ResponderTableComponent {
  responders = [
    { id: 1, name: 'John Doe', type: 'FIREFIGHTER', status: 'Available', location: 'Downtown' },
    { id: 2, name: 'Jane Smith', type: 'PARAMEDIC', status: 'On Call', location: 'Suburb' },
    { id: 3, name: 'Mike Johnson', type: 'POLICE_OFFICER', status: 'Available', location: 'City Center' },
    { id: 4, name: 'Emily Brown', type: 'HAZMAT_SPECIALIST', status: 'On Scene', location: 'Industrial Park' },
    { id: 5, name: 'Chris Lee', type: 'RESCUE_TEAM', status: 'Available', location: 'Coastal Area' },
  ];
}

