import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
// import { environment } from '../../../enviornments/enviornment';
import { Observable, throwError,tap } from 'rxjs';
import { Role, User } from '../../shared/models/user.model';
import { Router } from '@angular/router';

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

  private baseUrl = 'http://localhost:8888/login'; // Adjust to your login endpoint

  constructor(private http: HttpClient,
    private router: Router

  ) {}

  // login(loginRequest: { username: string; password: string; role: Role }): Observable<{ JwtToken: string }> {
  //   // console.log("wsedfrghyuj");
  //   return this.http.post<{ JwtToken: string }>(
  //     "http://localhost:8888/login",
  //     loginRequest
  //   ).pipe(
  //     tap((response) => {
  //       // Save the username to session storage
  //       sessionStorage.setItem('username', loginRequest.username);
  //     })
  //   );
   
  // }
 

  login(loginRequest: { username: string; password: string; role: Role }): Observable<{ jwtToken: string, userId: number | null, responderId: number | null }> {
    return this.http.post<{ jwtToken: string, userId: number | null, responderId: number | null }>(
      "http://localhost:8888/login",
      loginRequest
    ).pipe(
      tap((response) => {
        console.log('Login response: hello', response);
        sessionStorage.setItem('username', loginRequest.username);
      })
    );
  }
  
  saveToken(token: string): void {
    sessionStorage.setItem('jwt', token);
  }
 
  getToken(): string | null {
    return sessionStorage.getItem('jwt');
  }
 
  getRole(): string | null {
    const token = this.getToken();
 
    if (token) {
      try {
        // Decode the JWT token to extract the payload
        const payload = JSON.parse(atob(token.split('.')[1])); // Decodes the payload part of the JWT
 
        return payload.role || null;  // Return the role, or null if it's not available
      } catch (error) {
        console.error('Error decoding token:', error);
        return null;
      }
    }
    return null;
  }
 
  isAuthenticated(): boolean {
    return !!this.getToken();
  }
 
  logout(): void {
    sessionStorage.removeItem('jwt'); // Remove the specific 'jwt' item from sessionStorage
    sessionStorage.clear(); 
    this.router.navigate(['/login']);
    // Clear all items from sessionStorage
  }
 
 
  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-username/${username}`);
  }
 
  signup(user: User): Observable<string>
  {
 
    return this.http.post<string>(`${this.baseUrl}/register`, user);
  }
}
