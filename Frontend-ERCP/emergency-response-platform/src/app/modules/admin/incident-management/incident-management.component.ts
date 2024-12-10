import { Component, NgModule } from '@angular/core';
import { IncidentLogsComponent } from './incident-logs/incident-logs.component';
import { IncidentTableComponent } from './incident-table/incident-table.component';
import { MatTabsModule } from '@angular/material/tabs'
import { CommonModule } from '@angular/common';


@Component({
  standalone:true,
  imports:[IncidentLogsComponent,IncidentTableComponent,MatTabsModule,CommonModule],
  selector: 'app-incident-management',
  templateUrl: './incident-management.component.html',
  styleUrls: ['./incident-management.component.css'],
})
export class IncidentManagementComponent {
  selectedIncident: number | null = null;

  selectIncident(id: number): void {
    this.selectedIncident = id;
  }

  closeLogs(): void {
    this.selectedIncident = null;
  }
}

