// import { Routes } from '@angular/router';

// export const routes: Routes = [];

import { Routes } from '@angular/router';
import { LandingComponent } from './modules/public/landing/landing.component';
import { DashboardComponent } from '../app/modules/emergency/dashboard/dashboard.component';
import { LoginComponent } from '../app/modules/public/login/login.component';
export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'incident-tracking', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  // Other routes will be added later
];
