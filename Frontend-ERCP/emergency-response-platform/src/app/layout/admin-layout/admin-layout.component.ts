import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ResponderManagementComponent } from "../../modules/admin/responder-management/responder-management.component";
import { IncidentManagementComponent } from "../../modules/admin/incident-management/incident-management.component";
import { AdminDashboardComponent } from "../../modules/admin/dashboard/dashboard.component";
@Component({
  standalone: true,
  selector: 'app-admin-layout',
  imports: [
    CommonModule,
    RouterModule,
    ResponderManagementComponent,
    IncidentManagementComponent,
    AdminDashboardComponent
],
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit {
  // sidebarLinks = [
  //   { href: 'dashboard', label: 'Dashboard', content: 'dashboard' },
  //   { href: 'incident-management', label: 'Incident Management', content: 'incident-management' },
  //   { href: 'responder-management', label: 'Responder Management', content: 'responder-management' }
  // ];
  links = ['Dashboard', 'Incident Management', 'Responder Management'];
  selectedTab: string = this.links[0];
  constructor(
    private authService: AuthService
  ) {
    console.log('AdminLayoutComponent constructed');
  }

  ngOnInit() {
    console.log('AdminLayoutComponent initialized');
  }
  // add logout function
  logout() {
    console.log('Logging out');
    this.authService.logout();
  }
  setTab(tab: string) {
    this.selectedTab = tab;
  }
}