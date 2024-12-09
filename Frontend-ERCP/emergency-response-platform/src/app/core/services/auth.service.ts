// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {

//   constructor() { }
// }

// import { Injectable } from '@angular/core';
// import { Observable, of, throwError } from 'rxjs';
// import { User, LoginCredentials } from '../../shared/models/user.model';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthService {
//   // Simulated login method
//   login(credentials: LoginCredentials): Observable<User> {
//     // In a real app, this would be an HTTP call to your backend
//     if (credentials.username === 'admin' && credentials.password === 'emergency2024') {
//       return of({
//         id: '1',
//         username: 'admin',
//         email: 'admin@emergencyresponse.org',
//         role: 'Administrator',
//         department: 'Central Command'
//       });
//     }
//     return throwError(() => new Error('Invalid credentials'));
//   }

//   // Simulated logout method
//   logout(): void {
//     // Implement logout logic
//     console.log('User logged out');
//   }
// }


import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../enviornments/enviornment';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  role: string;
}

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, loginRequest).pipe(
      tap(response => {
        // Store token, user ID, and role in session storage
        sessionStorage.setItem('token', response.token);
        sessionStorage.setItem('userId', response.userId.toString());
        sessionStorage.setItem('userRole', response.role);
      })
    );
  }

  logout(): void {
    // Clear session storage on logout
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('userId');
    sessionStorage.removeItem('userRole');
  }

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem('token');
  }

  getUserRole(): string | null {
    return sessionStorage.getItem('userRole');
  }
}
