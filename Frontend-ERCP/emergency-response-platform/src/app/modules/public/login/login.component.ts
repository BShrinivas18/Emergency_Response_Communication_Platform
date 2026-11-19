import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { Role } from '../../../shared/models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  readonly loginForm = inject(FormBuilder).nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  loginError = '';
  pending = false;
  readonly sessionExpired = inject(ActivatedRoute).snapshot.queryParamMap.get('session') === 'expired';

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

  onSubmit(): void {
    if (this.loginForm.invalid || this.pending) return;

    const credentials = this.loginForm.getRawValue();
    this.pending = true;
    this.loginError = '';
    this.authService.login(credentials).pipe(finalize(() => this.pending = false)).subscribe({
      next: response => {
        const role = this.authService.startSession(response, credentials.username);
        if (role === Role.ADMIN) {
          void this.router.navigate(['/admin/dashboard']);
        } else if (role === Role.RESPONDER) {
          void this.router.navigate(['/responder-dashboard'], {
            queryParams: { responderId: response.responderId }
          });
        } else {
          this.loginError = 'The server did not return a usable staff session.';
        }
      },
      error: ({ status }) => {
        this.loginError = status === 401
          ? 'Invalid username or password.'
          : 'Sign-in is unavailable. Try again when the service is reachable.';
      }
    });
  }
}
