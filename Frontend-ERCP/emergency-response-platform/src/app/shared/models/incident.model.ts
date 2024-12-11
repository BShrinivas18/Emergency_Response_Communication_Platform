// src/app/shared/models/incident.model.ts
export interface Incident {
    incidentId: number;
    victimName: string;
    victimContact: string;
    victimLocation: {
      latitude: number;
      longitude: number;
      address?: string;
    };
    incidentLocation: {
      latitude: number;
      longitude: number;
      address?: string;
    };

    type: IncidentType;
    status: IncidentStatus;
    // description?: string;
   timestamp?: Date;
    responderIds: number[];

  }
  
  export enum IncidentType {
    MEDICAL_EMERGENCY = 'Medical Emergency',
    FIRE = 'Fire',
    ACCIDENT = 'Accident',
    CRIME = 'Crime',
    NATURAL_DISASTER = 'Natural Disaster',
    SOS_REQUEST = 'Emergency',
    HAZMAT = 'Chemical/Hazmat'
  }

  export const IncidentTypeMapping: Record<string, string> = {
    'Crime': 'CRIME',
    'Medical Emergency': 'MEDICAL_EMERGENCY',
    'Fire': 'FIRE',
    'Natural Disaster': 'NATURAL_DISASTER',
    'Chemical/Hazmat': 'HAZMAT',
    'Emergency': 'SOS_REQUEST',
  };;

  // MEDICAL_EMERGENCY,
  //   FIRE,
  //   NATURAL_DISASTER,
  //   HAZMAT,
  //   SOS_REQUEST,
  //   CRIME
  
  // export enum IncidentStatus {
  //   REPORTED = 'Reported',
  //   ACKNOWLEDGED = 'Acknowledged',
  //   IN_PROGRESS = 'In Progress',
  //   RESOLVED = 'Resolved'
  // }
  // export const IncidentStatusMapping: Record<string, string> = {
  //   'Reported': 'REPORTED',
  //   'Acknowledged': 'ACKNOWLEDGED',
  //   'In Progress': 'IN_PROGRESS',
  //   'Resolved': 'RESOLVED'
  // };

  export type IncidentStatus = 'Reported' | 'Assigned' | 'Enroute' | 'OnScene' | 'Resolved'|'NEW';

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
  
