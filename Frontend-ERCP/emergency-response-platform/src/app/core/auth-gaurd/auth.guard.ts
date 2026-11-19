import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = route => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const role = auth.getRole();
  if (!role) return router.createUrlTree(['/login']);

  const allowedRoles: string[] = route.data['roles'];
  return allowedRoles.includes(role) || router.createUrlTree(['/access-denied']);
};
