import { Component } from '@angular/core';
import { AdminDashboardComponent } from "./modules/admin/dashboard/dashboard.component";
import { IncidentManagementComponent } from './modules/admin/incident-management/incident-management.component';
import { IncidentManagementService } from './core/services/incident-management.service';
import { ResponderManagementComponent } from "./modules/admin/responder-management/responder-management.component";
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [AdminDashboardComponent, IncidentManagementComponent, ResponderManagementComponent,MatTabsModule],
  providers:[IncidentManagementService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'emergency-response-platform';
}
