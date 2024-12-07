// import { CommonModule } from '@angular/common';
// import { Component } from '@angular/core';
// import { MatIconModule } from '@angular/material/icon';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// import { ResponderTypeCardsComponent } from './responder-type-card/responder-type-card.component';
// import { ResponderFormComponent } from './responder-form/responder-form.component';
// import { ResponderDetailsComponent } from './responder-details/responder-details.component';


// @Component({
//   selector: 'app-responder-management',
//   standalone :true,
//   imports: [CommonModule,MatIconModule,ResponderTypeCardsComponent,ResponderFormComponent,ResponderDetailsComponent],
//   templateUrl: './responder-management.component.html',
//   styleUrl: './responder-management.component.css'
// })
// export class ResponderManagementComponent {

// }
import { Component } from '@angular/core';
import { ResponderFormComponent } from './responder-form/responder-form.component';
import { MatIconModule } from '@angular/material/icon';
import { ResponderTypeCardsComponent } from './responder-type-card/responder-type-card.component';
import { ResponderTableComponent } from './responder-table/responder-table.component';
import { ResponderDetailsComponent } from './responder-details/responder-details.component';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';

@Component({
  standalone:true,
  selector: 'app-responder-management',
  imports:[ResponderFormComponent,MatIconModule,ResponderTypeCardsComponent,ResponderTableComponent,ResponderDetailsComponent,MatCardModule,CommonModule],
  templateUrl: './responder-management.component.html',
  styleUrls: ['./responder-management.component.css']
})
export class ResponderManagementComponent {
  selectedResponderId: number | null = null;
  selectedResponderType: string | null = null;
  isCreating: boolean = false;

  onSelectResponderType(type: string) {
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
}

