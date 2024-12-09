// import { Component, Inject } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
// import { MatButtonModule } from '@angular/material/button';
// import { MatIconModule } from '@angular/material/icon';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-incident-submission-confirmation',
//   standalone: true,
//   imports: [
//     CommonModule, 
//     MatDialogModule, 
//     MatButtonModule, 
//     MatIconModule
//   ],
  
//   templateUrl: './incident-submission-confirmation.component.html',
//   styles: ['./incident-submission-confirmation.component.css']
// })
// export class IncidentSubmissionConfirmationComponent {
//   constructor(
//     public dialogRef: MatDialogRef<IncidentSubmissionConfirmationComponent>,
//     @Inject(MAT_DIALOG_DATA) public data: any,
//     private router: Router
//   ) {}

//   viewIncidentProgress() {
//     // Navigate to incident tracking page with incident ID
  
//     this.router.navigate(['/incident-tracking', this.data.id]);
//     this.dialogRef.close();
//   }
// }


import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';   
import { CommonModule } from '@angular/common';
import { Incident } from '../../models/incident.model';
import { MatCard, MatCardModule } from '@angular/material/card';
import { RouterModule, Router } from '@angular/router';
@Component({
  selector: 'app-confirmation-modal',
  standalone: true,
  imports: [
    CommonModule, 
    MatDialogModule, 
    MatButtonModule, 
    MatIconModule,
    MatCardModule,
    RouterModule
  ],
  templateUrl:  './incident-submission-confirmation.component.html',
  styleUrl: './incident-submission-confirmation.component.css',
})
export class IncidentSubmissionConfirmationComponent {
 
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, // Inject the data passed to the modal
    private dialogRef: MatDialogRef<IncidentSubmissionConfirmationComponent>,
    private router: Router
  ) {
//     // console.log(data);
//     // console.log(Object.keys(data));
//     // console.log(data.incident.victimAddress);
// // console.log(data.victimAddress);
// // console.log(data.victimContact);
// // console.log(data.victimName);
// // console.log(data.incidentLocation);

  }
ngOnInit() {
  console.log();
}
 
  onClose() {
    console.log('closing');
    // console.log(this.data);





    this.dialogRef.close();
  }

  viewProgress() {
    console.log(this.data);
    // alert('redirecting to progress report page');
    //redirect to google.com
    // window.location.href = 'https://www.google.com';
    // i want to redirect to the dashboard page
    this.router.navigate(['/incident-tracking']);
    this.dialogRef.close();
    // window.location.href = '/incident-tracking';
    // Redirect to the progress report page (replace with actual route)
    // window.location.href = 'google.com'; // Or use router.navigate
  }
}
