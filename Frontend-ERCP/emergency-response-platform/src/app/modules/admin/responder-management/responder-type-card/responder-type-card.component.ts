// import { CommonModule } from '@angular/common';
// import { Component, Output, EventEmitter } from '@angular/core';
// import { MatCardModule } from '@angular/material/card';
// import { MatIconModule } from '@angular/material/icon';

// interface ResponderTypeCard {
//   type: string;
//   total: number;
//   available: number;
// }

// @Component({
//   standalone:true,
//   imports:[MatCardModule,MatIconModule,CommonModule],
//   selector: 'app-responder-type-cards',
//   templateUrl: './responder-type-card.component.html',
//   styleUrls: ['./responder-type-card.component.css']
// })
// export class ResponderTypeCardsComponent {
//   @Output() selectType = new EventEmitter<string>();

//   responderTypes = [
//     { type: 'PARAMEDIC', label: 'Paramedic', icon: 'medical_services' },
//     { type: 'FIREFIGHTER', label: 'Firefighter', icon: 'local_fire_department' },
//     { type: 'POLICE_OFFICER', label: 'Police Officer', icon: 'security' },
//     { type: 'HAZMAT_SPECIALIST', label: 'Hazmat Specialist', icon: 'science' },
//     { type: 'RESCUE_TEAM', label: 'Rescue Team', icon: 'emoji_people' }
//   ];

//   selectResponderType(type: string) {
//     this.selectType.emit(type);
//   }
// }


import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ResponderType } from '../../../../core/services/responder.service';

interface ResponderTypeCardConfig {
  type: ResponderType;
  label: string;
  icon: string;
}

@Component({
  standalone: true,
  imports: [MatCardModule, MatIconModule, CommonModule],
  selector: 'app-responder-type-cards',
  templateUrl: './responder-type-card.component.html',
  styleUrls: ['./responder-type-card.component.css']
})
export class ResponderTypeCardsComponent {
  @Output() selectType = new EventEmitter<ResponderType>();

  responderTypes: ResponderTypeCardConfig[] = [
    { type: ResponderType.PARAMEDIC, label: 'Paramedic', icon: 'medical_services' },
    { type: ResponderType.FIREFIGHTER, label: 'Firefighter', icon: 'local_fire_department' },
    { type: ResponderType.POLICE_OFFICER, label: 'Police Officer', icon: 'security' },
    { type: ResponderType.HAZMAT_SPECIALIST, label: 'Hazmat Specialist', icon: 'science' },
    { type: ResponderType.RESCUE_TEAM, label: 'Rescue Team', icon: 'emoji_people' }
  ];

  selectResponderType(type: ResponderType) {
    this.selectType.emit(type);
  }
}
