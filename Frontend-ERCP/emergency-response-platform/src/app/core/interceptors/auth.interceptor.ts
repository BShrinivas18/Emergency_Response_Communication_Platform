import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { API_ORIGIN } from '../api.config';
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const publicRequest = request.url === `${API_ORIGIN}/incidents/create` || request.url === `${API_ORIGIN}/login`;
    if (publicRequest || !request.url.startsWith(API_ORIGIN + '/')) return next.handle(request);

    const token = this.auth.getToken();
    if (!token) return next.handle(request);

    return next.handle(request.clone({setHeaders: {Authorization: `Bearer ${token}`}})).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          this.auth.rejectSession(token);
        }
        return throwError(() => error);
      })
    );
  }
}
