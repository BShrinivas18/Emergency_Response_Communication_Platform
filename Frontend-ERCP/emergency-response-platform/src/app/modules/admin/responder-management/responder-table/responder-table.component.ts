// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-responder-table',
//   imports: [],
//   templateUrl: './responder-table.component.html',
//   styleUrl: './responder-table.component.css'
// })
// export class ResponderTableComponent {

// }
import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ResponderService } from '../../../../core/services/responder.service';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';

@Component({
  standalone:true,
  selector: 'app-responder-table',
  imports:[MatCardModule,MatTableModule],
  templateUrl: './responder-table.component.html',
  styleUrls: ['./responder-table.component.css']
})
export class ResponderTableComponent implements OnInit {
  @Input() responderType!: string;
  @Output() selectResponder = new EventEmitter<number>();

  responders: any[] = [];
  displayedColumns: string[] = ['id', 'name', 'status', 'actions'];

  constructor(private responderService: ResponderService) {}

  ngOnInit() {
    this.getResponders();
  }

  getResponders() {
    this.responderService.getRespondersByType(this.responderType).subscribe((data) => {
      this.responders = data;
    });
  }

  selectResponderById(id: number) {
    this.selectResponder.emit(id);
  }
}

