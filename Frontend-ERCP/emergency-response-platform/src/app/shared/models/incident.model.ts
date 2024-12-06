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
    description?: string;
    reportedAt?: Date;
  }
  
  export enum IncidentType {
    MEDICAL = 'Medical Emergency',
    FIRE = 'Fire',
    ACCIDENT = 'Accident',
    CRIME = 'Crime',
    NATURAL_DISASTER = 'Natural Disaster'
  }
  
  export enum IncidentStatus {
    REPORTED = 'Reported',
    ACKNOWLEDGED = 'Acknowledged',
    IN_PROGRESS = 'In Progress',
    RESOLVED = 'Resolved'
  }
