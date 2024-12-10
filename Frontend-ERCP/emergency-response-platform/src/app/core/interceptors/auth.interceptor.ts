import { Injectable } from '@angular/core';
import { 
  HttpInterceptor, 
  HttpRequest, 
  HttpHandler, 
  HttpEvent 
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>, 
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Retrieve the JWT token from local storage
    const token = localStorage.getItem('jwtToken');

    // If token exists, clone the request and add Authorization header
    if (token) {
      const clonedRequest = request.clone({
        headers: request.headers.set('Authorization', `Bearer ${token}`)
      });
      return next.handle(clonedRequest);
    }

    // If no token, proceed with the original request
    return next.handle(request);
  }
}
