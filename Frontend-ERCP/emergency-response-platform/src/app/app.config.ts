// // import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
// // import { provideRouter } from '@angular/router';

// // import { routes } from './app.routes';
// // import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

// // export const appConfig: ApplicationConfig = {
// //   providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes), provideAnimationsAsync()]
// // };
// import {BrowserAnimationsModule, provideAnimations } from '@angular/platform-browser/animations'
// import { ApplicationConfig } from '@angular/core';
// import { provideRouter } from '@angular/router';
// import { routes } from './app.routes';
// import { MatTableModule } from '@angular/material/table';
// import { MatTabsModule } from '@angular/material/tabs';
// import { MatIconModule } from '@angular/material/icon';
// import { MatButtonModule } from '@angular/material/button';
// import { BrowserModule } from '@angular/platform-browser';
// import { FormsModule } from '@angular/forms';
// import { MatCardModule } from '@angular/material/card';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatSelectModule } from '@angular/material/select';
// import { MatDialogModule } from '@angular/material/dialog';
// import { HttpClient } from '@angular/common/http';
// import { AppRoutingModule } from './modules/app-routing.module';
// // import { provideMatSortHeader } from '@angular/material';
// // import { GOOGLE_MAPS_API_CONFIG } from '@angular/google-maps';

// export const appConfig: ApplicationConfig = {
//   providers: [
//     provideRouter(routes),
//     provideAnimations(),
    
    
//     // {
//     //   provide: GOOGLE_MAPS_API_CONFIG,
//     //   useValue: {
//     //     apiKey: 'YOUR_GOOGLE_MAPS_API_KEY'
//     //   }
//     // }
//   ]
// };


//   imports:[
//     BrowserModule,
//     BrowserAnimationsModule,
//     FormsModule,
//     MatCardModule,
//     MatFormFieldModule,
//     MatInputModule,
//     MatSelectModule,
//     MatTableModule,
//     MatTabsModule,
//     MatIconModule,
//     MatButtonModule,
//     MatDialogModule,
//     HttpClient,
//     AppRoutingModule
    
//   ]

import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';

// Material Module Imports
import { importProvidersFrom } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { BrowserModule } from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    provideRouter(routes),
    provideAnimations(),
    importProvidersFrom([
     BrowserModule,
     HttpClientModule,
      FormsModule,
      MatCardModule,
      MatFormFieldModule,
      MatInputModule,
      MatSelectModule,
      MatTableModule,
      MatTabsModule,
      MatIconModule,
      MatButtonModule,
      MatDialogModule,
      AuthInterceptor
    ])
  ]
};
