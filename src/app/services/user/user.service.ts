import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../../dtos/user/register.dto';
import { LoginDTO } from '../../dtos/user/login.dto';
import { envipronment } from '../../environments/environment';
import { LoginResponse } from '../../responses/user/login.response';
import { UserResponse } from '../../responses/user/user.response';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly USER_ID_KEY: string = 'user_id';
  private readonly USER_RESPONSE_KEY: string = "user";
  private userUrl = `${envipronment.apiPrefix}/users`;
  httpHeaders = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
    })
  };

  httpHeadersIncludeToken = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      "Add-Token": "true",
    })
  };

  constructor (private httpClient: HttpClient) {

  }

  register (registerDTO: RegisterDTO) : Observable<LoginResponse> {
    const url = `/register`;
    return this.httpClient.post<LoginResponse>(`${this.userUrl}${url}`, registerDTO,this.httpHeaders);
  }

  login (loginDTO: LoginDTO): Observable<any> {
    const url = `/login`;
    return this.httpClient.post<LoginResponse>(`${this.userUrl}${url}`, loginDTO, this.httpHeaders);
  }

  getUserDetails () : Observable<UserResponse> {
    const url = `/details`;
    return this.httpClient.post<UserResponse>(`${this.userUrl}${url}`, null, this.httpHeadersIncludeToken);
  }

  setId(user_id:number) : void {
    localStorage.setItem(this.USER_ID_KEY, user_id.toString());
  }
  
  getId() : number {
    return Number(localStorage.getItem(this.USER_ID_KEY))|0;
  }

  removeId() : void {
    localStorage.removeItem(this.USER_ID_KEY);
  }

  setUser (userResponse: UserResponse) : void {
    localStorage.setItem(this.USER_RESPONSE_KEY, JSON.stringify(userResponse));
  }

  getUser () : UserResponse {
    console.log("user", localStorage.getItem(this.USER_RESPONSE_KEY)??"");
    return JSON.parse(localStorage.getItem(this.USER_RESPONSE_KEY)??"");
  }

  removeUser () : void {
    localStorage.removeItem(this.USER_RESPONSE_KEY);
  }
}
