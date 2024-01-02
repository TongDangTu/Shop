import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { LoginResponse } from '../../responses/user/login.response'
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  
  phoneNumber: string;
  password: string;
  

  constructor (private router:Router, private userService: UserService, private tokenService: TokenService) {
    this.phoneNumber="33445566";
    this.password="123456";
  }

  onPhoneNumberChange () {
    console.log(`Phone:${this.phoneNumber}`);
  }

  

  login () {
    alert(`${this.phoneNumber}`
      + `\n${this.password}`);
    const loginDTO: LoginDTO = new LoginDTO ({
      "phone_number": this.phoneNumber,
      "password": this.password
    });

    this.userService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        alert(response.message);
        const token = response.token;
        this.tokenService.setToken(token);
        // this.router.navigate(['']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        alert(`${error.error.message}`);
      }
    });
  }

}
