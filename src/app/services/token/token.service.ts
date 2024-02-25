import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'access_token';
  private jwtHelperService: JwtHelperService = new JwtHelperService;
  
  constructor () {

  }

  getToken() : string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token:string) : void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  removeToken() : void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getUserId(): number {
    var user = this.jwtHelperService.decodeToken(this.getToken()??"");
    return user.user_id??0;
  }

  isExpired(): boolean {
    if (this.getToken() == null) {
      return false;
    }
    return this.jwtHelperService.isTokenExpired(this.getToken());
  }
}
