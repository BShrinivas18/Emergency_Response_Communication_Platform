
import { AdminDashboardComponent } from './modules/admin/dashboard/dashboard.component';
import { IncidentManagementComponent } from './modules/admin/incident-management/incident-management.component';
import { ResponderManagementComponent } from './modules/admin/responder-management/responder-management.component';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';
import { LandingComponent } from './modules/public/landing/landing.component';
import { DashboardComponent } from '../app/modules/emergency/dashboard/dashboard.component';
import { LoginComponent } from '../app/modules/public/login/login.component';
import { Routes } from '@angular/router';
import { ResponderDashboardComponent } from './modules/responder/dashboard/dashboard.component';
import { authGuard } from './core/auth-gaurd/auth.guard';
import { AccessDeniedComponent } from './core/access-denied/access-denied.component';
export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'incident-tracking', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  {path :'access-denied', component: AccessDeniedComponent},
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate : [authGuard],
    data : { roles : ['ADMIN']},
    // Wrap routes with AdminLayoutComponent
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'incident-management', component: IncidentManagementComponent },
      { path: 'responder-management', component: ResponderManagementComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }  // Default route
    ]
  },
  {path: 'responder-dashboard', component: ResponderDashboardComponent ,
    canActivate : [authGuard],
    data:{roles : ['ADMIN', 'RESPONDER']} 
  },
  { path: '', redirectTo: 'admin/dashboard', pathMatch: 'full' }, // Root redirect
  { path: '**', redirectTo: 'admin/dashboard' }
  // Other routes will be added later

];
