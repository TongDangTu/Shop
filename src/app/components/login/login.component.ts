import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user/user.service';
import { Router } from '@angular/router';
import { LoginResponse } from '../../responses/user/login.response'
import { TokenService } from '../../services/token/token.service';
import { RoleService } from '../../services/role/role.service';
import { Role } from '../../models/role';
import { UserResponse } from '../../responses/user/user.response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  
  phoneNumber: string;
  password: string;
  roles: Role[] = [];
  isRemember: boolean = true;
  selectedRole: Role | undefined;

  userResponse: UserResponse = {
    id: 0,
    address: '',
    fullname: '',
    phone_number: '',
    is_active: false,
    date_of_birth: 0,
    facebook_account_id: 0,
    goole_account_id: 0,
    role_name: "",
  };

  constructor (
    private router:Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) {
    this.phoneNumber="";
    this.password="";
  }

  // @Override
  ngOnInit () {
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        this.roles = roles;
        if (roles.length > 0) {
          this.selectedRole = roles[0];
        }
        else {
          this.selectedRole = undefined;
        }
      },
      complete: () => {

      },
      error: (error: any) => {
        alert(error.error.message);
      }
    });
  }

  login () {
    const loginDTO: LoginDTO = new LoginDTO ({
      "phone_number": this.phoneNumber,
      "password": this.password,
    });

    this.userService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        const token = response.token;
        if (this.isRemember) {
        }
        this.tokenService.setToken(token);
        this.getUserDetails();
      },
      complete: () => {
        
      },
      error: (error: any) => {
        alert(`${error.error.message}`);
      }
    });
  }

  getUserDetails () {
    this.userService.getUserDetails().subscribe({
      next: (response: UserResponse) => {
        this.userResponse = response;
        this.userResponse.date_of_birth = new Date(this.userResponse.date_of_birth);
        this.userService.setUser(this.userResponse);
        this.userService.setId(this.userResponse.id);
      },
      complete: () => {
        if (this.userService.getUser().role_name == "admin") {
          this.router.navigateByUrl('/dashboard_admin');
        }
        else {
          this.router.navigateByUrl('/');
        }
      },
      error: (error: any) => {
        alert(`${error.error.message}`);
      }
    });
  }
}
