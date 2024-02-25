import { Injectable, inject } from '@angular/core';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate, UrlTree, CanActivateFn } from '@angular/router';
import { TokenService } from '../token/token.service' ;
import { Observable } from 'rxjs';
@Injectable()
export class AuthGuard implements CanActivate {
  constructor(public tokenService: TokenService, public router: Router) {}
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
      if (this.tokenService.getUserId() != 0 && this.tokenService.isExpired()) {
        return true;
      }
      else {
        this.router.navigate(["/login"]);
        return false;
      }
  }
}

export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  return inject(AuthGuard).canActivate(next, state);
}