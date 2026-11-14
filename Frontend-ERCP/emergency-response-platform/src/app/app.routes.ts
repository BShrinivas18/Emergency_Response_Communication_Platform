import { Routes } from '@angular/router';
import { authGuard } from './core/auth-gaurd/auth.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', loadComponent: () => import('./modules/public/landing/landing.component').then(m => m.LandingComponent) },
  { path: 'incident-tracking', loadComponent: () => import('./modules/emergency/dashboard/dashboard.component').then(m => m.DashboardComponent) },
  { path: 'login', loadComponent: () => import('./modules/public/login/login.component').then(m => m.LoginComponent) },
  { path: 'access-denied', loadComponent: () => import('./core/access-denied/access-denied.component').then(m => m.AccessDeniedComponent) },
  {
    path: 'admin',
    loadComponent: () => import('./layout/admin-layout/admin-layout.component').then(m => m.AdminLayoutComponent),
    canActivate: [authGuard], data: { roles: ['ADMIN'] },
    children: [
      { path: 'dashboard', loadComponent: () => import('./modules/admin/dashboard/dashboard.component').then(m => m.AdminDashboardComponent) },
      { path: 'incident-management', loadComponent: () => import('./modules/admin/incident-management/incident-management.component').then(m => m.IncidentManagementComponent) },
      { path: 'responder-management', loadComponent: () => import('./modules/admin/responder-management/responder-management.component').then(m => m.ResponderManagementComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: 'responder-dashboard', loadComponent: () => import('./modules/responder/dashboard/dashboard.component').then(m => m.ResponderDashboardComponent),
    canActivate: [authGuard], data: { roles: ['ADMIN', 'RESPONDER'] } },
  { path: '**', redirectTo: '' }
];
