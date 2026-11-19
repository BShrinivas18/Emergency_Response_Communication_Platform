import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { finalize, timeout } from 'rxjs';
import { Role } from '../../../shared/models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, MatButtonModule],
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

  onSubmit(): void {
    if (this.loginForm.invalid || this.pending) return;

    const {username, password} = this.loginForm.getRawValue();
    const credentials = {username: username.trim(), password};
    this.pending = true;
    this.loginError = '';
    this.authService.login(credentials).pipe(timeout(15000), finalize(() => this.pending = false)).subscribe({
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
          : status === 422
            ? 'This account needs a matching responder profile. Contact your platform administrator.'
            : 'Sign-in is unavailable. Try again when the service is reachable.';
      }
    });
  }
}
