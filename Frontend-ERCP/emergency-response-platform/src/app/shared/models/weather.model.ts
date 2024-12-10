export interface WeatherModel {
    type: string; // Corresponds to 'event' in the API
    description: string; // Corresponds to 'description' in the API
    severity: 'low' | 'medium' | 'high'; // Custom mapped based on API tags
    location: string;
}
