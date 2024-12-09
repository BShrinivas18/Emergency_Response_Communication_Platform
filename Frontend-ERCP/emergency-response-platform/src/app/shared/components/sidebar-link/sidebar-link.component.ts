import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-sidebar-link',
  imports: [
    CommonModule, 
    RouterModule
  ],
  template: `
    <a 
      [routerLink]="['/admin', href]" 
      class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100 transition-colors"
    >
      <ng-content></ng-content>
    </a>
  `
})
export class SidebarLinkComponent {
  @Input() href: string = '';
}