// import { Injectable } from '@angular/core';

// @Injectable({
//   providedIn: 'root'
// })
// export class WeatherService {

//   constructor() { }
// }

// src/app/core/services/weather.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface WeatherAlert {
  type: string;
  description: string;
  severity: 'low' | 'medium' | 'high';
  location: string;
}

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private apiKey = 'YOUR_OPENWEATHERMAP_API_KEY'; // Replace with actual API key
  private baseUrl = 'https://api.openweathermap.org/data/2.5';

  constructor(private http: HttpClient) {}

  getWeatherAlerts(lat: number, lon: number): Observable<WeatherAlert[]> {
    return this.http.get<WeatherAlert[]>(`${this.baseUrl}/onecall`, {
      params: {
        lat: lat.toString(),
        lon: lon.toString(),
        appid: this.apiKey,
        exclude: 'current,minutely,hourly,daily'
      }
    });
  }
}
