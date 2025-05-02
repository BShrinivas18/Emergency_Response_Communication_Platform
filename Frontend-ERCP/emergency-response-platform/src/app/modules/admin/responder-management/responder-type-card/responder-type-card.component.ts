
import { CommonModule } from '@angular/common';
import { Component, Output, EventEmitter } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ResponderType } from '../../../../core/services/responder.service';

// interface ResponderTypeCardConfig {
//   type: ResponderType;
//   label: string;
// }

@Component({
  standalone: true,
  imports: [MatCardModule, MatIconModule, CommonModule],
  selector: 'app-responder-type-cards',
  templateUrl: './responder-type-card.component.html',
  styleUrls: ['./responder-type-card.component.css']
})
export class ResponderTypeCardsComponent {
  @Output() selectType = new EventEmitter<ResponderType>();

  responderTypes = [
    { type: ResponderType.PARAMEDIC, label: 'Paramedic', icon: 'healing' },
    { type: ResponderType.FIREFIGHTER, label: 'Firefighter', icon: 'local_fire_department' },
    { type: ResponderType.POLICE_OFFICER, label: 'Police Officer', icon: 'local_police' },
    { type: ResponderType.HAZMAT_SPECIALIST, label: 'Hazmat Specialist', icon: 'dangerous' },
    { type: ResponderType.RESCUE_TEAM, label: 'Rescue Team', icon: 'search' }
  ];

  selectResponderType(type: ResponderType) {
    this.selectType.emit(type);
  }
}
