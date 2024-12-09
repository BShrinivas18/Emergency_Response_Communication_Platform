// import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';



// @NgModule({
//   declarations: [],
//   imports: [
//     CommonModule
//   ]
// })
// export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Import your components (adjust paths if needed)
import { AdminDashboardComponent } from './admin/dashboard/dashboard.component';
import { IncidentManagementComponent } from './admin/incident-management/incident-management.component';
import { ResponderManagementComponent } from './admin/responder-management/responder-management.component';

const routes: Routes = [
  {
    path: 'admin',
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'incident-management', component: IncidentManagementComponent },
      { path: 'responder-management', component: ResponderManagementComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }  // Default route
    ]
  },
  { path: '**', redirectTo: 'admin/dashboard' }  // Catch-all route for any invalid paths
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],  // Configure the router with these routes
  exports: [RouterModule]  // Export RouterModule to use it in other modules
})
export class AppRoutingModule { }

