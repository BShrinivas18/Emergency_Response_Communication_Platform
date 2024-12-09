

// import { Component, Input } from '@angular/core';
// import { Stage, IncidentStatus } from '../../../shared/models/incident.model';
// import { CommonModule } from '@angular/common';
// @Component({
//   selector: 'app-incident-progress',
//   standalone: true,
//   imports: [CommonModule],
//   templateUrl: './incident-progress.component.html',
//   styleUrls: ['./incident-progress.component.css']
// })
// export class IncidentProgressComponent {
//   @Input() stages!: Stage[];
//   @Input() currentStatus!: IncidentStatus;

//   getStageClass(stage: Stage): string {
//     const currentIndex = this.stages.findIndex(s => s.status === this.currentStatus);
//     const stageIndex = this.stages.findIndex(s => s.status === stage.status);
    
//     if (stageIndex <= currentIndex) return 'completed';
//     return 'pending';
//   }

//   isActive(stage: Stage): boolean {
//     return stage.status === this.currentStatus;
//   }
//   getProgressPercentage(): string {
//     const completedStages = this.stages.filter(stage => this.isActive(stage)).length;
//     return `${(completedStages / this.stages.length) * 100}%`;
//   }
// }



import { Component, Input } from '@angular/core';
import { Stage, IncidentStatus } from '../../../shared/models/incident.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-incident-progress',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './incident-progress.component.html',
  styleUrls: ['./incident-progress.component.css']
})
export class IncidentProgressComponent {
  @Input() stages!: Stage[];
  @Input() currentStatus!: IncidentStatus;

  isActive(stage: Stage): boolean {
    return stage.status === this.currentStatus || this.isStageCompleted(stage);
  }

  isStageCompleted(stage: Stage): boolean {
    const currentIndex = this.stages.findIndex(s => s.status === this.currentStatus);
    const stageIndex = this.stages.findIndex(s => s.status === stage.status);
    return stageIndex <= currentIndex;
  }

  getMarkerPosition(): string {
    const currentIndex = this.stages.findIndex(stage => stage.status === this.currentStatus);
    const totalStages = this.stages.length;

    // Calculate marker position based on the current stage
    return `${(currentIndex / (totalStages - 1)) * 100}%`;
  }

  getCompletedLineHeight(): string {
    const currentIndex = this.stages.findIndex(stage => stage.status === this.currentStatus);
    const totalStages = this.stages.length;

    // Calculate height of the completed line
    return `${(currentIndex / (totalStages - 1)) * 100}%`;
  }
}

