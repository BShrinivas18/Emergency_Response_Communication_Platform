import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ResponderType } from '../../../core/services/responder.service';
import { ResponderTypeCardsComponent } from './responder-type-card/responder-type-card.component';
import { ResponderTableComponent } from './responder-table/responder-table.component';
import { ResponderDetailsComponent } from './responder-details/responder-details.component';
import { ResponderFormComponent } from './responder-form/responder-form.component';

@Component({
  standalone: true,
  selector: 'app-responder-management',
  imports: [
    MatIconModule,
    ResponderTypeCardsComponent,
    ResponderTableComponent,
    ResponderDetailsComponent,
    MatCardModule,
    CommonModule,
    MatButtonModule,
    MatDialogModule,

  ],
  templateUrl: './responder-management.component.html',
  styleUrls: ['./responder-management.component.css']
})
export class ResponderManagementComponent {
  selectedResponderId: number | null = null;
  selectedResponderType: ResponderType | null = null;

  constructor(private dialog: MatDialog) {}

  onSelectResponderType(type: ResponderType) {
    this.selectedResponderType = type;
  }

  onSelectResponder(id: number) {
    this.selectedResponderId = id;
  }

  closeDetails() {
    this.selectedResponderId = null;
  }

  openCreateResponderForm() {
    const dialogRef = this.dialog.open(ResponderFormComponent, {
      width: '500px',
      height: 'auto',
      panelClass: 'custom-dialog'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Handle the created responder if needed
        console.log('New responder created', result);
      }
    });
  }
}