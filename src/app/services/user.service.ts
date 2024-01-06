import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { envipronment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiRegister = `${envipronment.apiBaseUrl}/users/register`;
  private apiLogin = `${envipronment.apiBaseUrl}/users/login`;
  apiHeaders = {
    headers: new HttpHeaders("Content-Type: application/json")
  };

  constructor (private httpClient: HttpClient) {

  }

  register (registerDTO: RegisterDTO) : Observable<any> {
    return this.httpClient.post(this.apiRegister, registerDTO,this.apiHeaders);
  }

  login (loginDTO: LoginDTO): Observable<any> {
    return this.httpClient.post(this.apiLogin, loginDTO, this.apiHeaders);
  }
}
