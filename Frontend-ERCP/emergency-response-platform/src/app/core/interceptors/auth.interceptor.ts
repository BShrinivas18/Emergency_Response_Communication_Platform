import { Injectable } from '@angular/core';
import { 
  HttpInterceptor, 
  HttpRequest, 
  HttpHandler, 
  HttpEvent, 
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
   
  constructor(private router:Router){}

  intercept(
    request: HttpRequest<any>, 
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Retrieve the JWT token from local storage
    const token = localStorage.getItem('jwtToken');

    // If token exists, clone the request and add Authorization header
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
 

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 || error.status === 403) {
          // Token invalid or expired
          localStorage.removeItem('jwtToken'); // Optional: remove token
          this.router.navigate(['/login']);    // Redirect to login
        }
        return throwError(() => error); // Re-throw the error
      })
    );
    // If no token, proceed with the original request
    // return next.handle(request);
  }
}
