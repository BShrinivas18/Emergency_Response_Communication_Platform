import { Component } from '@angular/core';

// import { RouterOutlet } from '@angular/router';
import { LandingComponent } from './modules/public/landing/landing.component';
import { DashboardComponent } from './modules/emergency/dashboard/dashboard.component';
import { IncidentSubmissionConfirmationComponent } from './shared/components/incident-submission-confirmation/incident-submission-confirmation.component';
import { RouterModule } from '@angular/router';
import { LoginComponent } from './modules/public/login/login.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'

})
export class AppComponent {
  title = 'emergency-response-platform';
}
