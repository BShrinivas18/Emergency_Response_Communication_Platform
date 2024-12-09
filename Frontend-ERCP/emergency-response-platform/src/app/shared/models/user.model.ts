// export class UserModel {
// }

export interface User {
    id: string;
    username: string;
    email: string;
    role: 'Dispatcher' | 'First Responder' | 'Coordinator' | 'Administrator';
    department: string;
  }
  
  export interface LoginCredentials {
    username: string;
    password: string;
  }
