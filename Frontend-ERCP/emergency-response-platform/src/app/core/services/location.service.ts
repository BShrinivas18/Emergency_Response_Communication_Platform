import { Injectable } from '@angular/core';
import { Observable, from, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
// import { environment } from '../../../enviornments/enviornment';

export interface LocationData {
  latitude: number;
  longitude: number;
  accuracy?: number;
  address?: string;
  formattedAddress?: string;
  streetNumber?: string;
  route?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
  neighborhood?: string;
}

export interface LocationError {
  code: number;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private geocoder: google.maps.Geocoder;

  constructor(private http: HttpClient) {
    // Initialize Google Maps Geocoder
    this.geocoder = new google.maps.Geocoder();
  }

  /**
   * Request location permission from the user
   * @returns Promise resolving to boolean indicating permission status
   */
  requestLocationPermission(): Promise<boolean> {
    return new Promise((resolve) => {
      // Check if geolocation is supported
      if (!navigator.geolocation) {
        resolve(false);
        return;
      }

      // Request permission
      navigator.geolocation.getCurrentPosition(
        () => resolve(true),  // Permission granted
        () => resolve(false), // Permission denied
        { timeout: 10000 }
      );
    });
  }

  /**
   * Get current location using browser's geolocation API
   * @returns Promise resolving to LocationData
   */
  getCurrentLocation(): Promise<LocationData> {
    return new Promise((resolve, reject) => {
      // Check if geolocation is supported
      if (!navigator.geolocation) {
        reject({ 
          code: 1, 
          message: 'Geolocation is not supported by this browser.' 
        });
        return;
      }

      // Request location with high accuracy
      navigator.geolocation.getCurrentPosition(
        async (position) => {
          try {
            const locationDetails = await this.getAddressFromCoordinates(
              position.coords.latitude, 
              position.coords.longitude
            );
            
            resolve({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              accuracy: position.coords.accuracy,
              ...locationDetails
            });
          } catch (error) {
            // If geocoding fails, return basic location
            resolve({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              accuracy: position.coords.accuracy
            });
          }
        },
        (error) => {
          let errorMessage = '';
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMessage = 'User denied location access';
              break;
            case error.POSITION_UNAVAILABLE:
              errorMessage = 'Location information unavailable';
              break;
            case error.TIMEOUT:
              errorMessage = 'Location request timed out';
              break;
            default:
              errorMessage = 'An unknown error occurred';
          }

          reject({
            code: error.code,
            message: errorMessage
          });
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0
        }
      );
    });
  }

  /**
   * Get detailed address from coordinates using Google Maps Geocoder
   * @param latitude 
   * @param longitude 
   * @returns Promise with detailed location information
   */
  getAddressFromCoordinates(
    latitude: number, 
    longitude: number
  ): Promise<Partial<LocationData>> {
    return new Promise((resolve, reject) => {
      const latLng = new google.maps.LatLng(latitude, longitude);

      this.geocoder.geocode({ location: latLng }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK && results && results[0]) {
          const addressComponents = results[0].address_components;
          const locationInfo: Partial<LocationData> = {
            formattedAddress: results[0].formatted_address
          };

          // Extract detailed address components
          addressComponents.forEach((component) => {
            const types = component.types;

            if (types.includes('street_number')) 
              locationInfo.streetNumber = component.long_name;
            
            if (types.includes('route')) 
              locationInfo.route = component.long_name;
            
            if (types.includes('neighborhood')) 
              locationInfo.neighborhood = component.long_name;
            
            if (types.includes('locality')) 
              locationInfo.city = component.long_name;
            
            if (types.includes('administrative_area_level_1')) 
              locationInfo.state = component.long_name;
            
            if (types.includes('country')) 
              locationInfo.country = component.long_name;
            
            if (types.includes('postal_code')) 
              locationInfo.postalCode = component.long_name;
          });

          resolve(locationInfo);
        } else {
          reject(`Geocoding failed: ${status}`);
        }
      });
    });
  }

  /**
   * Get coordinates from an address
   * @param address 
   * @returns Promise with latitude and longitude
   */
  getCoordinatesFromAddress(address: string): Promise<{lat: number, lng: number}> {
    return new Promise((resolve, reject) => {
      this.geocoder.geocode({ address: address }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK && results && results[0]) {
          const location = results[0].geometry.location;
          resolve({
            lat: location.lat(),
            lng: location.lng()
          });
        } else {
          reject(`Geocoding failed: ${status}`);
        }
      });
    });
  }

  /**
   * Watch location changes
   * @returns Observable of LocationData
   */
  watchLocation(): Observable<LocationData> {
    return new Observable((observer) => {
      const watchId = navigator.geolocation.watchPosition(
        async (position) => {
          try {
            const locationDetails = await this.getAddressFromCoordinates(
              position.coords.latitude, 
              position.coords.longitude
            );
            
            observer.next({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              accuracy: position.coords.accuracy,
              ...locationDetails
            });
          } catch (error) {
            observer.next({
              latitude: position.coords.latitude,
              longitude: position.coords.longitude,
              accuracy: position.coords.accuracy
            });
          }
        },
        (error) => {
          let errorMessage = '';
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMessage = 'User denied location access';
              break;
            case error.POSITION_UNAVAILABLE:
              errorMessage = 'Location information unavailable';
              break;
            case error.TIMEOUT:
              errorMessage = 'Location request timed out';
              break;
            default:
              errorMessage = 'An unknown error occurred';
          }

          observer.error({
            code: error.code,
            message: errorMessage
          });
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,          
          maximumAge: 0
        }
      );

      // Cleanup function
      return () => {
        navigator.geolocation.clearWatch(watchId);
      };
    });
  }

  /**
   * Calculate distance between two coordinates
   * @param lat1 
   * @param lon1 
   * @param lat2 
   * @param lon2 
   * @returns Distance in kilometers
   */
  calculateDistance(
    lat1: number, 
    lon1: number, 
    lat2: number, 
    lon2: number
  ): number {
    const R = 6371; // Radius of the earth in km
    const dLat = this.deg2rad(lat2 - lat1);
    const dLon = this.deg2rad(lon2 - lon1);
    const a = 
      Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
      Math.sin(dLon/2) * Math.sin(dLon/2)
    ;
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c; // Distance in kilometers
  }

  /**
   * Convert degrees to radians
   * @param deg 
   * @returns Radians
   */
  private deg2rad(deg: number): number {
    return deg * (Math.PI/180);
  }

  /**
   * Check if location services are available
   * @returns Boolean indicating location service availability
   */
  isLocationServiceAvailable(): boolean {
    return 'geolocation' in navigator;
  }
}