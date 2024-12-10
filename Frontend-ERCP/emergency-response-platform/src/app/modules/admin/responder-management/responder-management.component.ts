import { Component } from '@angular/core';
import { ResponderFormComponent } from './responder-form/responder-form.component';
import { MatIconModule } from '@angular/material/icon';
import { ResponderTypeCardsComponent } from './responder-type-card/responder-type-card.component';
import { ResponderTableComponent } from './responder-table/responder-table.component';
import { ResponderDetailsComponent } from './responder-details/responder-details.component';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ResponderType } from '../../../core/services/responder.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@Component({
  standalone:true,
  selector: 'app-responder-management',
  imports:[
    MatIconModule,
    ResponderTypeCardsComponent,
    ResponderTableComponent,
    ResponderDetailsComponent,
    MatCardModule,
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    
  ],
  templateUrl: './responder-management.component.html',
  styleUrls: ['./responder-management.component.css']
})
export class ResponderManagementComponent {
  selectedResponderId: number | null = null;
  selectedResponderType: ResponderType | null = null;
  isCreating: boolean = false;

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

  closeForm() {
    this.isCreating = false;
  }

  openCreateResponderForm() {
    const dialogRef = this.dialog.open(ResponderFormComponent, {
      width: '500px', // Set the width of the dialog
      height: 'auto', // Height can be adjusted automatically
      panelClass: 'custom-dialog', // Optional: Custom CSS class for styling the dialog
    });


    dialogRef.afterClosed().subscribe(result => {
      // Handle the form close event, if necessary
      console.log('The dialog was closed');
    });
  }
}

