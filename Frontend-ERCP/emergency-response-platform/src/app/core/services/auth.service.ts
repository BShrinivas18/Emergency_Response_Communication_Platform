import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../enviornments/enviornment';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

interface LoginCredentials {
  username: string;
  password: string;
}

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

  private apiUrl = 'http://localhost:8888/login'; // Adjust to your login endpoint

  constructor(private http: HttpClient) {}

  login(credentials: LoginCredentials): Observable<any> {
    return this.http.post(this.apiUrl, credentials).pipe(
      tap((response: any) => {
        if (response.token) {
          // Store the JWT token in localStorage
          localStorage.setItem('jwtToken', response.token);
        }
      }),
      catchError(error => {
        // Handle the error gracefully
        console.error('Login failed', error);
        return throwError(error);
      })
    );
  }

  logout() {
    // Remove the JWT token from local storage
    localStorage.removeItem('jwtToken');
  }

  isLoggedIn(): boolean {
    // Check if a valid JWT token exists
    const token = localStorage.getItem('jwtToken');
    return !!token; // You can add additional validation here to check if the token is expired

  
  }
}
