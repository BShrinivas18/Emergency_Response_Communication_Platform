// src/app/shared/models/incident.model.ts
export interface Incident {
    id?: string;
    victimName: string;
    victimContact: string;
    location: {
      latitude: number;
      longitude: number;
      address?: string;
    };
    incidentType: IncidentType;
    status: IncidentStatus;
    // description?: string;
    reportedAt?: Date;
  }
  
  export enum IncidentType {
    MEDICAL = 'Medical Emergency',
    FIRE = 'Fire',
    ACCIDENT = 'Accident',
    CRIME = 'Crime',
    NATURAL_DISASTER = 'Natural Disaster',
    EMERGENCY = 'Emergency'
  }
  
  // export enum IncidentStatus {
  //   REPORTED = 'Reported',
  //   ACKNOWLEDGED = 'Acknowledged',
  //   IN_PROGRESS = 'In Progress',
  //   RESOLVED = 'Resolved'
  // }

  export type IncidentStatus = 'Reported' | 'Assigned' | 'Enroute' | 'OnScene' | 'Resolved';

export interface IncidentReport {
  id: string;
  description: string;
  location: string;
  reportedAt: Date;
  status: IncidentStatus;
  priority: 'Low' | 'Medium' | 'High' | 'Critical';
}

export interface Stage {
  name: string;
  status: IncidentStatus;
  icon: string;
  completedAt?: Date;
}

export interface CommunicationEntry {
  id: string;
  timestamp: Date;
  sender: string;
  message: string;
}
  
