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
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // onSubmit() {
  //   if (this.loginForm.valid) {
  //     this.authService.login(this.loginForm.value).subscribe({
  //       next: (user) => {
  //         console.log('Login successful', user);
  //         // Navigate to dashboard or home page
  //         this.router.navigate(['/incident-tracking']);
  //       },
  //       error: (err) => {
  //         this.loginError = 'Invalid username or password';
  //       }
  //     });
  //   }
  // }
  onSubmit(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;

      this.authService.login({ username, password }).subscribe({
        next: (response) => {
          // Redirect based on role
          switch (response.role) {
            case 'ADMIN':
              this.router.navigate(['/admin-dashboard']);
              break;
            case 'USER':
              this.router.navigate(['/user-dashboard']);
              break;
            default:
              this.router.navigate(['/']);
          }
        },
        error: (error) => {
          this.loginError = 'Invalid username or password';
          console.error('Login error', error);
        }
      });
    }
  }
}


