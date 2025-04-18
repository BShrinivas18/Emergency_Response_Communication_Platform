import { Component } from '@angular/core';
import {  MatDialogModule } from '@angular/material/dialog';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-warning-dialog',
  imports: [MatDialogModule, MatIcon ],
  templateUrl: './warning-dialog.component.html',
  styleUrl: './warning-dialog.component.css'
})
export class WarningDialogComponent {

}
