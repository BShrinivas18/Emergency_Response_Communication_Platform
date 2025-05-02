
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ResponderService, ResponderType, ResponderDTO } from '../../../../core/services/responder.service';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ResponderDetailsComponent } from '../responder-details/responder-details.component';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  standalone: true,
  selector: 'app-responder-table',
  imports: [CommonModule, MatTableModule, MatIconModule, MatButtonModule, MatCardModule, MatDialogModule,MatFormFieldModule],
  templateUrl: './responder-table.component.html',
  styleUrls: ['./responder-table.component.css'],
})
export class ResponderTableComponent implements OnInit {
  @Input() responderType!: ResponderType;
  @Output() selectResponder = new EventEmitter<number>();

  responders: ResponderDTO[] = [];
  displayedColumns: string[] = ['id', 'name', 'status', 'actions'];

  constructor(
    private responderService: ResponderService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.fetchRespondersByType();
  }

  fetchRespondersByType() {
    this.responderService.getRespondersByType(this.responderType)
      .subscribe({
        next: (responders) => {
          this.responders = responders;
        },
        error: (error) => {
          console.error('Error fetching responders', error);
        }
      });
  }

  selectResponderById(id: number) {
    this.selectResponder.emit(id);
  }



  openResponderDetails(responderId: number) {
    const dialogRef = this.dialog.open(ResponderDetailsComponent, {
      data: { responderId: responderId },
      width: '500px'
    });
  }

  changeResponderType(responder: ResponderDTO, newType: ResponderType) {
    // Create a copy of the responder with the new type
    const updatedResponder: ResponderDTO = {
      ...responder,
      type: newType
    };

    // Call service to update the responder
    this.responderService.updateResponder(responder.responderId, updatedResponder)
      .subscribe({
        next: () => {
          // Refresh the list of responders
          this.fetchRespondersByType();
        },
        error: (error) => {
          console.error('Error changing responder type', error);
        }
      });
  }


  
}

