import { Component } from '@angular/core';
import { TokenService } from '../../services/token/token.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  currentUrl: string = '';
  navs: navItem[] = [
    {url: "/", title: "Trang chủ", icon: "fa-solid fa-house"},
    {url: "/intro", title: "Giới thiệu", icon: "fa-solid fa-circle-info"},
    {url: "/carts", title: "Giỏ hàng", icon: "fa-solid fa-cart-shopping"},
    {url: "/dashboard_admin", title: "Quản trị", icon: "fa-solid fa-gauge-high"},
  ]
  token: string = '';
  fullname: string = "";

  constructor (private tokenService: TokenService, private userServive: UserService, private router: Router) {

  }

  ngOnInit() {
    this.currentUrl = this.router.url;
    this.token = this.tokenService.getToken() || '';
    this.fullname = this.userServive.getUser().fullname;
  }

  logOut() {
    this.tokenService.removeToken();
    this.userServive.removeUser();
    this.userServive.removeId();
    this.token = "";
    this.router.navigateByUrl("/login");
  }
}

interface navItem {
  url: string,
  title: string,
  icon: string,
}
