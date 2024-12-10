// import { Component, Input, Output, EventEmitter, OnInit, Inject } from '@angular/core';
// import { ResponderService } from '../../../../core/services/responder.service';
// import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
// import { MatCardModule } from '@angular/material/card';
// import { ResponderFormComponent } from '../responder-form/responder-form.component';

// @Component({
//   standalone: true,
//   imports:[MatDialogModule,MatCardModule,ResponderFormComponent],
//   selector: 'app-responder-details',
//   templateUrl: './responder-details.component.html',
//   styleUrls: ['./responder-details.component.css'],
// })
// export class ResponderDetailsComponent implements OnInit {
//   @Input() responderId!: number;
//   @Output() close = new EventEmitter<void>();

//   responder: any;
//   isEditing = false;

//   constructor(@Inject(MAT_DIALOG_DATA) public data:any,
//     private dialogRef: MatDialogRef<ResponderDetailsComponent>,
//     private responderService: ResponderService) {}

//   ngOnInit() {
//     // this.responder = this.responderService.getResponderById(this.data.responderId);
//   }

//   closeDetails() {
//     this.dialogRef.close();
//   }

//   // updateResponder(updatedResponder: any) {
//   //   this.responderService.updateResponder(updatedResponder);
//   //   this.responder = updatedResponder;
//   //   this.isEditing = false;
//   // }
// }


import { Component, Input, Output, EventEmitter, OnInit, Inject } from '@angular/core';
import { ResponderService, ResponderDTO } from '../../../../core/services/responder.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatCardModule } from '@angular/material/card';
import { ResponderFormComponent } from '../responder-form/responder-form.component';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [MatDialogModule, MatCardModule,MatButtonModule, CommonModule,ResponderFormComponent],
  selector: 'app-responder-details',
  templateUrl: './responder-details.component.html',
  styleUrls: ['./responder-details.component.css'],
})
export class ResponderDetailsComponent implements OnInit {
  @Input() responderId!: number;
  @Output() close = new EventEmitter<void>();

  responder!: ResponderDTO;
  isEditing = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { responderId: number },
    private dialogRef: MatDialogRef<ResponderDetailsComponent>,
    private responderService: ResponderService
  ) {}

  ngOnInit() {
    this.fetchResponderDetails();
  }

  fetchResponderDetails() {
    this.responderService.getResponderById(this.data.responderId)
      .subscribe({
        next: (responder) => {
          this.responder = responder;
        },
        error: (error) => {
          console.error('Error fetching responder details', error);
        }
      });
  }

  closeDetails() {
    this.dialogRef.close();
  }

  startEditing() {
    this.isEditing = true;
  }

  updateResponder(updatedResponder: ResponderDTO) {
    this.responderService.updateResponder(this.responder.responderId!, updatedResponder)
      .subscribe({
        next: (responder) => {
          this.responder = responder;
          this.isEditing = false;
        },
        error: (error) => {
          console.error('Error updating responder', error);
        }
      });
  }
}