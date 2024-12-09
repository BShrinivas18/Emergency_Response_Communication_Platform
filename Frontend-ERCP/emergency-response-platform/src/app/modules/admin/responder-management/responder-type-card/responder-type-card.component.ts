// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-responder-type-card',
//   imports: [],
//   templateUrl: './responder-type-card.component.html',
//   styleUrl: './responder-type-card.component.css'
// })
// export class ResponderTypeCardComponent {

// }

import { Component, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone:true,
  imports:[MatCardModule,MatIconModule],
  selector: 'app-responder-type-cards',
  templateUrl: './responder-type-card.component.html',
  styleUrls: ['./responder-type-card.component.css']
})
export class ResponderTypeCardsComponent {
  @Output() selectType = new EventEmitter<string>();

  responderTypes = [
    { type: 'PARAMEDIC', label: 'Paramedic', icon: 'medical_services' },
    { type: 'FIREFIGHTER', label: 'Firefighter', icon: 'local_fire_department' },
    { type: 'POLICE_OFFICER', label: 'Police Officer', icon: 'security' },
    { type: 'HAZMAT_SPECIALIST', label: 'Hazmat Specialist', icon: 'science' },
    { type: 'RESCUE_TEAM', label: 'Rescue Team', icon: 'emoji_people' }
  ];

  selectResponderType(type: string) {
    this.selectType.emit(type);
  }
}

