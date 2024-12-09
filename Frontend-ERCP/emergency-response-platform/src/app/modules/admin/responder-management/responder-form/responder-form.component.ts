// import { Component } from '@angular/core';

// @Component({
//   standalone:true,
//   selector: 'app-responder-form',
//   imports: [],
//   templateUrl: './responder-form.component.html',
//   styleUrl: './responder-form.component.css'
// })
// export class ResponderFormComponent {

// }

import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-responder-form',
  standalone:true,
  imports:[MatFormFieldModule,CommonModule,MatCardModule,MatSelectModule,FormsModule],
  templateUrl: './responder-form.component.html',
  styleUrls: ['./responder-form.component.css']
})
export class ResponderFormComponent {
  @Input() responder?: any;
  @Output() close = new EventEmitter<void>();
  @Output() submit = new EventEmitter<any>();

  name: string = '';
  stationLocation: string = '';
  status: string = '';
  type: string = '';

  RESPONDER_STATUSES = [
    'AVAILABLE', 'ASSIGNED', 'ON_ROUTE', 'REACHED_LOCATION',
    'OFFLINE', 'ON_SCENE', 'NOT_AVAILABLE', 'OFF_DUTY'
  ];

  RESPONDER_TYPES = [
    'PARAMEDIC', 'FIREFIGHTER', 'POLICE_OFFICER',
    'HAZMAT_SPECIALIST', 'RESCUE_TEAM'
  ];

  ngOnInit() {
    if (this.responder) {
      this.name = this.responder.name;
      this.stationLocation = this.responder.stationLocation;
      this.status = this.responder.status;
      this.type = this.responder.type;
    }
  }

  handleSubmit() {
    const updatedResponder = {
      id: this.responder?.id || Date.now(),
      name: this.name,
      stationLocation: this.stationLocation,
      status: this.status,
      type: this.type
    };
    this.submit.emit(updatedResponder);
    this.close.emit();
  }
}

