import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route : ActivatedRouteSnapshot) => {
  const authservice= inject(AuthService);
  const router = inject(Router);

  const allowedRoles: string[] = route.data['roles'];
  const userrole = authservice.getRole();
if(authservice.isAuthenticated() && allowedRoles.includes(userrole || '')){
  return true;
}
else {
  
  // window.location.href = "https://www.google.com/";
  // return false;
 return router.navigate(['/access-denied']);
}
};
