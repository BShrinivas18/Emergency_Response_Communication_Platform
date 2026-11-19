import { API_ORIGIN } from '../api.config';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginCredentials, Role, User } from '../../shared/models/user.model';
import { Router } from '@angular/router';

export interface LoginResponse {
  jwtToken: string;
  userId: number | null;
  responderId: number | null;
}

function readRole(token: unknown): Role | null {
  if (typeof token !== 'string') return null;
  const parts = token.split('.');
  if (parts.length !== 3 || parts.some(part => !part)) return null;
  try {
    const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
    if (!Number.isFinite(payload.exp) || payload.exp <= Date.now() / 1000) return null;
    return payload.role === Role.ADMIN || payload.role === Role.RESPONDER ? payload.role : null;
  } catch {
    return null;
  }
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: LoginCredentials): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_ORIGIN}/login`, credentials);
  }

  startSession(response: LoginResponse | null, username: string): Role | null {
    const role = readRole(response?.jwtToken ?? null);
    const id = role === Role.ADMIN ? response?.userId : response?.responderId;
    if (!response || !role || typeof id !== 'number' || !Number.isSafeInteger(id) || id <= 0) return null;

    this.clearSession();
    sessionStorage.setItem('jwt', response.jwtToken);
    sessionStorage.setItem('username', username);
    sessionStorage.setItem(role === Role.ADMIN ? 'userId' : 'responderId', String(id));
    return role;
  }

  getToken(): string | null {
    return sessionStorage.getItem('jwt');
  }

  getRole(): Role | null {
    // Use the claims for navigation only. The gateway verifies credentials and permissions.
    return readRole(this.getToken());
  }

  rejectSession(token: string): void {
    if (this.getToken() === token) {
      this.clearSession();
      void this.router.navigate(['/login'], { queryParams: { session: 'expired' } });
    }
  }

  logout(): void {
    this.clearSession();
    void this.router.navigate(['/login']);
  }

  private clearSession(): void {
    for (const key of ['jwt', 'username', 'userId', 'responderId']) {
      sessionStorage.removeItem(key);
    }
  }

  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${API_ORIGIN}/check-username/${username}`);
  }

  signup(user: User): Observable<string> {
    return this.http.post<string>(`${API_ORIGIN}/register`, user);
  }

  provisionResponder(username: string, password: string): Observable<Pick<User, 'id' | 'username' | 'role'>> {
    return this.http.post<Pick<User, 'id' | 'username' | 'role'>>(`${API_ORIGIN}/register`, {
      username, password, role: Role.RESPONDER
    });
  }
}
