// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-dashboard-card',
//   imports: [],
//   templateUrl: './dashboard-card.component.html',
//   styleUrl: './dashboard-card.component.css'
// })
// export class DashboardCardComponent {

// }

import { Component, Input } from '@angular/core';

@Component({
  standalone:true,
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.css']
})
export class DashboardCardComponent {
  @Input() title!: string;
  @Input() value!: string;
  @Input() icon!: string;
}

