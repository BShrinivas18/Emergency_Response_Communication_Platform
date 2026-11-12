import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
export interface WeatherAlert {
  type: string;
  description: string;
  severity: 'low' | 'medium' | 'high';
  location: string;
}

@Injectable({providedIn: 'root'})
export class WeatherService {
  // Disabled until an authenticated server-side weather provider is configured.
  getWeatherAlerts(lat: number, lon: number): Observable<WeatherAlert[]> { return of([]); }
}
