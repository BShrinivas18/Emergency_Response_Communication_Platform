
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-incident',
  standalone: true,
  imports: [ CommonModule, ReactiveFormsModule],
  templateUrl: './active-incidents.component.html', 
  styleUrls: ['./active-incidents.component.css']
})
export class IncidentComponent {
  responderForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.responderForm = this.fb.group({
      responderCount: [1, [Validators.required, Validators.min(1)]]
    });
  }

  requestResponders() {
    if (this.responderForm.valid) {
      const count = this.responderForm.get('responderCount')?.value;
      alert(`Requested ${count} additional responder(s)`);
    }
  }
}