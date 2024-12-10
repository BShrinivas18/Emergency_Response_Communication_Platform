
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
interface Incident {
  id: string;
  type: string;
  date: string;
  status: string;
}

@Component({
  selector: 'app-incident-history',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './past-incidents.component.html', 
  styleUrls: ['./past-incidents.component.css']
  
})
export class IncidentHistoryComponent {
  incidents: Incident[] = [
    { id: 'INC-2023-005', type: 'Medical', date: '2023-06-01', status: 'Resolved' },
    { id: 'INC-2023-004', type: 'Fire', date: '2023-05-28', status: 'Resolved' },
    { id: 'INC-2023-003', type: 'Rescue', date: '2023-05-25', status: 'Resolved' },
    { id: 'INC-2023-002', type: 'Hazmat', date: '2023-05-20', status: 'Resolved' },
  ];
}

