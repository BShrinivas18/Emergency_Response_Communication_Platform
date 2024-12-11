// import { Component } from '@angular/core';

// @Component({
//   selector: 'app-login',
//   imports: [],
//   templateUrl: './login.component.html',
//   styleUrl: './login.component.css'
// })
// export class LoginComponent {

// }

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { IncidentManagementService } from '../../../core/services/incident-management.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  loginError: string = '';
  
  // Statistical placeholders for emergency response platform
  emergencyStats = [
    { label: 'Total Incidents This Year', value: '12,547' },
    { label: 'Average Response Time', value: '8.3 mins' },
    { label: 'Lives Saved', value: '2,345' }
  ];

  featuredServices = [
    {
      title: 'Rapid Dispatch',
      description: 'Advanced routing and real-time resource allocation',
      icon: 'flash_on'
    },
    {
      title: 'Incident Tracking',
      description: 'Comprehensive monitoring and reporting system',
      icon: 'track_changes'
    },
    {
      title: 'Resource Management',
      description: 'Optimize personnel and equipment deployment',
      icon: 'people'
    }
  ];

  constructor(
    private fb: FormBuilder, 
    private authService: AuthService,
    private router: Router,
    private http: HttpClient,
    private incidentManagementService: IncidentManagementService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['']
    });
  }

  onSubmit() {
    const loginRequest = {
     username: this.loginForm.get('username')!.value,
     password: this.loginForm.get('password')!.value, 
      role: this.loginForm.get('role')!.value
    };

    // const { username, password, role } = loginRequest;
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (token) => {
          console.log('Login successful', token);
          this.authService.saveToken(token.JwtToken);
          this.incidentManagementService.getUserId().subscribe({
            next: (userId) => {
              if(userId!==null){
                sessionStorage.setItem('userId', userId.toString());
              
              console.log('User ID:', userId);
              if(loginRequest.role ==="ADMIN"){
                this.router.navigate(['/admin-dashboard']);
              }
            }
              // else if(loginRequest.role==="RESPONDER"){
              //   this.router.navigate(['/responder-dashboard']);
              // }
            },
            error: (err) => {
              console.error('Error retrieving user ID:', err);
            }
          });
          // Navigate to dashboard or home page
          // this.router.navigate(['/admin-dashboard']);
        },
       
        error: (err) => {
          this.loginError = 'Invalid username or password';
        }
        
      });
    }
  }
//   onSubmit() {
//     if (this.loginForm.valid) {
//       const { username, password, role } = this.loginForm.value;
      
//       this.http.post<any>('http://localhost:8888/login', { 
//         username, 
//         password,
//         role
//       }).subscribe({
//         next: (response) => {
//           // Store JWT token in session storage (as per your existing code)
//           sessionStorage.setItem('jwt', response.jwtToken);
//           sessionStorage.setItem('username', username);

//           // Route based on role
//           switch(role) {
//             case 'ADMIN':
//               this.router.navigate(['/admin']);
//               break;
//             case 'RESPONDER':
//               this.router.navigate(['/responder-dashboard']);
//               break;
//             default:
//               this.router.navigate(['/']);
//           }
//         },
//         error: (err) => {
//           this.errorMessage = 'Login failed. Please check your credentials.';
//           console.error('Login error', err);
//         }
//       });
//     }
//   }
//   // onSubmit(): void {
//   //   if (this.loginForm.valid) {
//   //     const { username, password } = this.loginForm.value;

//   //     this.authService.login({ username, password }).subscribe({
//   //       next: (response) => {
//   //         // Redirect based on role
//   //         switch (response.role) {
//   //           case 'ADMIN':
//   //             this.router.navigate(['dashboard']);
//   //             break;
//   //           case 'RESPONDER':
//   //             this.router.navigate(['/responder']);
//   //             break;
//   //           default:
//   //             this.router.navigate(['/']);
//   //         }
//   //       },
//   //       error: (error) => {
//   //         this.loginError = 'Invalid username or password';
//   //         console.error('Login error', error);
//   //       }
//   //     });
//   //   }
//   // }
}


