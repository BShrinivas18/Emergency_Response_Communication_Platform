import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { IncidentManagementService } from './core/services/incident-management.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  providers: [IncidentManagementService],
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {
  title = 'emergency-response-platform';
}
