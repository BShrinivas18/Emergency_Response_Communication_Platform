// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-incident-logs',
//   imports: [],
//   templateUrl: './incident-logs.component.html',
//   styleUrl: './incident-logs.component.css'
// })
// export class IncidentLogsComponent {

// }
import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { MatIcon } from '@angular/material/icon';

@Component({
  standalone:true,
  imports:[CommonModule,MatIcon],
  selector: 'app-incident-logs',
  templateUrl: './incident-logs.component.html',
  styleUrls: ['./incident-logs.component.css'],
})
export class IncidentLogsComponent implements OnInit, OnDestroy {
  @Input() incidentId: number |any ;
  @Output() close = new EventEmitter<void>();

  logs = [
    { id: 1, timestamp: '2023-06-01 10:00', message: 'Incident reported' },
    { id: 2, timestamp: '2023-06-01 10:05', message: 'Responders dispatched' },
    { id: 3, timestamp: '2023-06-01 10:15', message: 'Responders arrived on scene' },
    { id: 4, timestamp: '2023-06-01 10:30', message: 'Situation assessment completed' },
    { id: 5, timestamp: '2023-06-01 11:00', message: 'Additional resources requested' },
  ];

  ngOnInit(): void {
    document.body.style.overflow = 'hidden';
  }

  ngOnDestroy(): void {
    document.body.style.overflow = 'unset';
  }

  closeLogs(): void {
    this.close.emit();
  }
}

