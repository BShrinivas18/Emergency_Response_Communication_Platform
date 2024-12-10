// export class UserModel {
// }

export interface User {
    id: number;
    username: string;
    email: string;
    role: Role;
    // role: 'Dispatcher' | 'First Responder' | 'Coordinator' | 'Administrator';
    // department: string;
  }
  export enum Role {
    ADMIN = 'ADMIN',
    RESPONDER = 'RESPONDER'
  }
  
  export interface LoginCredentials {
    username: string;
    password: string;
  }
