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
import { FormsModule } from '@angular/forms';
import { finalize, timeout } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

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
    FormsModule

  ],
  templateUrl: './responder-management.component.html',
  styleUrls: ['./responder-management.component.css']
})
export class ResponderManagementComponent {
  selectedResponderId: number | null = null;
  selectedResponderType: ResponderType | null = null;

  showAccountForm = false;
  accountName = '';
  accountPassword = '';
  accountPending = false;
  accountError = '';
  accountCreated = '';

  constructor(private dialog: MatDialog, private auth: AuthService) {}

  provisionResponder(): void {
    const name = this.accountName.trim();
    if (this.accountPending || !name || this.accountPassword.length < 12) return;

    this.accountPending = true;
    this.accountError = '';
    this.accountCreated = '';
    this.auth.provisionResponder(name, this.accountPassword)
      .pipe(timeout(15000), finalize(() => this.accountPending = false))
      .subscribe({
        next: account => {
          this.accountPassword = '';
          this.accountName = '';
          this.accountCreated = account.username;
          this.showAccountForm = false;
        },
        error: error => {
          switch (error.status) {
            case 409: this.accountError = 'This sign-in already exists.'; break;
            case 422: this.accountError = 'Create a responder profile with that exact name first.'; break;
            case 400: this.accountError = 'Use a valid username and a password of at least 12 characters and at most 72 bytes.'; break;
            default: this.accountError = 'Could not confirm account creation. Check whether it exists before retrying.';
          }
        }
      });
  }

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

      }
    });
  }
}
