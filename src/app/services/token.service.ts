import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'access_token';
  
  constructor () {

  }

//   localStorage là một đối tượng lưu trữ dữ liệu trên trình duyệt của thiết bị
//   Dữ liệu này sẽ được giữ giữa các SESSION

  getToken() : string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token:string) : void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  removeToken() : void {
    localStorage.removeItem(this.TOKEN_KEY);
  }
}
