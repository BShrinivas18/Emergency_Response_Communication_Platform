
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LogService, LogDTO } from '../../../../core/services/log.service';

// Angular Material imports
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  standalone: true,
  selector: 'app-incident-log-table',
  templateUrl: './incident-log-table.component.html',
  styleUrls: ['./incident-log-table.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule
  ]
})
export class IncidentLogTableComponent implements OnInit {
  incidentLogs: LogDTO[] = [];

  // Sorting
  sortColumn: keyof LogDTO | null = null;
  sortDirection: 'asc' | 'desc' = 'asc';

  // Filtering
  filterColumn: keyof LogDTO | null = null;
  filterValue: string = '';
  filterDate: Date | null = null;

  constructor(private logService: LogService) {}

  ngOnInit() {
    this.loadIncidentLogs();
  }

  loadIncidentLogs() {
    this.logService.getAllLogs().subscribe({
      next: (data: LogDTO[]) => {
        this.incidentLogs = data;
      },
      error: (error) => {
        console.error('Error fetching incident logs:', error);
      }
    });
  }

  get filteredAndSortedLogs(): LogDTO[] {
    let logs = [...this.incidentLogs];

    // Filter
    if (this.filterColumn && (this.filterValue.trim() || this.filterDate)) {
      logs = logs.filter(log => {
        const value = log[this.filterColumn!];
        if (value == null) return false;

        if (this.filterColumn === 'timestamp') {
          if (!this.filterDate) return true;
          const logDate = new Date(value).toISOString().split('T')[0];
          const filterDateStr = this.filterDate.toISOString().split('T')[0];
          return logDate === filterDateStr;
        }

        const filter = this.filterValue.trim().toLowerCase();
        return value.toString().toLowerCase().includes(filter);
      });
    }

    // Sort
    if (this.sortColumn) {
      logs.sort((a, b) => {
        const valA = a[this.sortColumn!];
        const valB = b[this.sortColumn!];

        if (valA == null || valB == null) return 0;

        return this.sortDirection === 'asc'
          ? valA > valB ? 1 : valA < valB ? -1 : 0
          : valA < valB ? 1 : valA > valB ? -1 : 0;
      });
    }

    return logs;
  }

  changeSort(event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    this.sortColumn = value ? value as keyof LogDTO : null;
  }

  changeFilter(event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    this.filterColumn = value ? value as keyof LogDTO : null;

    // Clear any previous filter input
    this.filterValue = '';
    this.filterDate = null;
  }

  toggleSortDirection() {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
  }

  clearFilter() {
    this.filterColumn = null;
    this.filterValue = '';
    this.filterDate = null;
  }
}

