import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminSidebarComponent } from '../../shared/components/admin-sidebar/admin-sidebar.component';

@Component({
  standalone: true,
  selector: 'app-admin-layout',
  imports: [
    CommonModule, 
    RouterModule, 
    AdminSidebarComponent
  ],
  template: `
    <div class="flex h-screen overflow-hidden bg-gray-100">
      <app-admin-sidebar></app-admin-sidebar>
      <main class="flex-1 overflow-y-auto p-8">
        <div class="mx-auto max-w-7xl">
          <router-outlet></router-outlet>
        </div>
      </main>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      height: 100vh;
      width: 100vw;
    }
  `]
})
export class AdminLayoutComponent implements OnInit {
  constructor() {
    console.log('AdminLayoutComponent constructed');
  }

  ngOnInit() {
    console.log('AdminLayoutComponent initialized');
  }
}