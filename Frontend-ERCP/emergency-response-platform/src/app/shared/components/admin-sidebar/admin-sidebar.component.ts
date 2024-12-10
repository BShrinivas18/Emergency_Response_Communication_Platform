import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SidebarLinkComponent } from '../sidebar-link/sidebar-link.component';

@Component({
  standalone: true,
  selector: 'app-admin-sidebar',
  imports: [
    CommonModule, 
    RouterModule,
    SidebarLinkComponent
  ],
  template: `
    <div class="w-64 flex-shrink-0 bg-white shadow-lg">
      <div class="flex h-16 items-center justify-center border-b border-gray-200">
        <h1 class="text-2xl font-bold text-gray-800">
          Admin Panel
        </h1>
      </div>
      <nav class="space-y-2 p-4">
        <app-sidebar-link 
          *ngFor="let link of sidebarLinks" 
          [href]="link.href"
          [style.color]="'black'"
        >
          {{ link.label }}
        </app-sidebar-link>
      </nav>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      height: 100%;
    }
  `]
})
export class AdminSidebarComponent implements OnInit {
  sidebarLinks = [
    { href: 'dashboard', label: 'Dashboard' },
    { href: 'incident-management', label: 'Incident Management' },
    { href: 'responder-management', label: 'Responder Management' }
  ];

  constructor() {
    console.log('AdminSidebarComponent constructed');
  }

  ngOnInit() {
    console.log('AdminSidebarComponent initialized');
  }
}