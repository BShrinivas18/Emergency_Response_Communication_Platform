// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class ResponderService {

//   constructor() { }
// }

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResponderService {
  private baseUrl = 'http://localhost:8080/api/responders'; // Change to your actual API base URL

  constructor(private http: HttpClient) {}

  getRespondersByType(type: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}?type=${type}`);
  }

  getResponderById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  createResponder(responder: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, responder);
  }

  updateResponder(id: number, responder: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, responder);
  }

  deleteResponder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

