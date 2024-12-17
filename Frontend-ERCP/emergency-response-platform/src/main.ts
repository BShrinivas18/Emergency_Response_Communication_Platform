// main.ts

import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { environment } from './environments/environment';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));

// Dynamically create and inject the Google Maps script tag
const googleMapsApiKey = environment.googleMapsApiKey;

// Create a new script element
const script = document.createElement('script');
script.src = `https://maps.googleapis.com/maps/api/js?key=${googleMapsApiKey}&libraries=places`;
script.async = true;
script.defer = true;

// Append the script tag to the document head
document.head.appendChild(script);
