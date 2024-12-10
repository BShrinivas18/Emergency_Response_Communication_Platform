
import { AdminDashboardComponent } from './modules/admin/dashboard/dashboard.component';
import { IncidentManagementComponent } from './modules/admin/incident-management/incident-management.component';
import { ResponderManagementComponent } from './modules/admin/responder-management/responder-management.component';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { LandingComponent } from './modules/public/landing/landing.component';
import { DashboardComponent } from '../app/modules/emergency/dashboard/dashboard.component';
import { LoginComponent } from '../app/modules/public/login/login.component';
export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'incident-tracking', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  {
    path: 'admin',
    component: AdminLayoutComponent, // Wrap routes with AdminLayoutComponent
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'incident-management', component: IncidentManagementComponent },
      { path: 'responder-management', component: ResponderManagementComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }  // Default route
    ]
  },
  { path: '', redirectTo: 'admin/dashboard', pathMatch: 'full' }, // Root redirect
  { path: '**', redirectTo: 'admin/dashboard' }
  // Other routes will be added later

];
