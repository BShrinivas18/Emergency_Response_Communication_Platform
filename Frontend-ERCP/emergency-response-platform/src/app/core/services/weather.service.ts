
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
  private apiKey = 'ea69bd232640b6859c7269d38c0d2308'; // Replace with actual API key
  private baseUrl = 'https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&appid={apiKey}';

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

  // fetchWeatherAlerts() {
  //   const mockAlerts: WeatherAlert[] = [
  //     {
  //       type: 'Flood Warning',
  //       description: 'Heavy rainfall expected in your area.',
  //       severity: 'high',
  //       location: 'New York, USA',
  //     },
  //     {
  //       type: 'Heat Advisory',
  //       description: 'Extreme temperatures likely. Stay hydrated.',
  //       severity: 'medium',
  //       location: 'California, USA',
  //     },
  //   ];
  
  //   // Simulate API response using mock data
  //   this.weatherAlertsSubject.next(mockAlerts);
  
  //   // Uncomment for actual API call
  //   const { lat, lng } = this.mapCenter();
  //   this.weatherService.getWeatherAlerts(lat, lng).subscribe({
  //     next: (alerts) => {
  //       this.weatherAlertsSubject.next(alerts);
  //     },
  //     error: (err) => {
  //       console.error('Weather alerts fetch failed', err);
  //     }
  //   });
  // }
  
}
