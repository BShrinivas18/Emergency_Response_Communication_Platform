import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
// import { environment } from '../../../enviornments/enviornment';
import { Observable, throwError,tap } from 'rxjs';
// import { catchError } from 'rxjs/operators';
import { Role, User } from '../../shared/models/user.model';
// import { coerceStringArray } from '@angular/cdk/coercion';
import { Router } from '@angular/router';
// interface LoginCredentials {
//   username: string;
//   password: string;
//   role: string;
// }

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

  // login(credentials: LoginCredentials): Observable<any> {
  //   return this.http.post(this.apiUrl, credentials).pipe(
  //     tap((response: any) => {
  //       if (response.token) {
  //         // Store the JWT token in localStorage
  //         localStorage.setItem('jwtToken', response.token);
  //       }
  //     }),
  //     catchError(error => {
  //       // Handle the error gracefully
  //       console.error('Login failed', error);
  //       return throwError(error);
  //     })
  //   );
  // }

  // logout() {
  //   // Remove the JWT token from local storage
  //   localStorage.removeItem('jwtToken');
  // }

  // isLoggedIn(): boolean {
  //   // Check if a valid JWT token exists
  //   const token = localStorage.getItem('jwtToken');
  //   return !!token; // You can add additional validation here to check if the token is expired

  
  // }
  login(loginRequest: { username: string; password: string; role: Role }): Observable<{ JwtToken: string }> {
    // console.log("wsedfrghyuj");
    return this.http.post<{ JwtToken: string }>(
      "http://localhost:8888/login",
      loginRequest
    ).pipe(
      tap((response) => {
        // Save the username to session storage
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
 
  // getUserId(){
  //   const username = sessionStorage.getItem('username');
 
  // }
 
 
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
