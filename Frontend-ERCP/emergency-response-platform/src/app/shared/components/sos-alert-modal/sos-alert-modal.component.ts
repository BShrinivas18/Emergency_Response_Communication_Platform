import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-sos-alert-confirmation',
  standalone: true,
  imports: [
    CommonModule, 
    MatDialogModule, 
    MatButtonModule, 
    MatIconModule
  ],
  templateUrl:'./sos-alert-modal.component.html',
  styleUrls: ['./sos-alert-modal.component.css']

})
export class SOSAlertConfirmationComponent {
  constructor(
    public dialogRef: MatDialogRef<SOSAlertConfirmationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    console.log(data);
     }
}
