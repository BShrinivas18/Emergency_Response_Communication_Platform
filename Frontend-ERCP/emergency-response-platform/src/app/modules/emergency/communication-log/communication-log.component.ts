// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-communication-log',
//   imports: [],
//   templateUrl: './communication-log.component.html',
//   styleUrl: './communication-log.component.css'
// })
// export class CommunicationLogComponent {

// }

import { Component, Input } from '@angular/core';
import { CommunicationEntry } from '../../../shared/models/incident.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-communication-log',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './communication-log.component.html',
  styleUrls: ['./communication-log.component.css']
})
export class CommunicationLogComponent {
  @Input() entries!: CommunicationEntry[];
}
