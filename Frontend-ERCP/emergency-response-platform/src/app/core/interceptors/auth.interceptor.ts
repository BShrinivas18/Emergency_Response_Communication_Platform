import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { API_ORIGIN } from '../api.config';
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const publicRequest = request.url === `${API_ORIGIN}/incidents/create` || request.url === `${API_ORIGIN}/login`;
    const token = this.auth.getToken();
    if (token && !publicRequest && request.url.startsWith(API_ORIGIN + '/')) {
      request = request.clone({setHeaders: {Authorization: `Bearer ${token}`}});
    }
    return next.handle(request);
  }
}
