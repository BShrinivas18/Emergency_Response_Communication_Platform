// import { Routes } from '@angular/router';

// export const routes: Routes = [];

// import { Routes } from '@angular/router';
// import { LandingComponent } from './modules/public/landing/landing.component';

// export const routes: Routes = [
//   { path: '', component: LandingComponent },
//   // Other routes will be added later
// ];

import { Routes } from '@angular/router';

// Import your components (adjust paths if needed)
import { AdminDashboardComponent } from './modules/admin/dashboard/dashboard.component';
import { IncidentManagementComponent } from './modules/admin/incident-management/incident-management.component';
import { ResponderManagementComponent } from './modules/admin/responder-management/responder-management.component';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';

export const routes: Routes = [
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
  { path: '**', redirectTo: 'admin/dashboard' }  // Catch-all route for any invalid paths
];
