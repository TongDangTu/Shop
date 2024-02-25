import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryService } from '../../services/category/category.service';
import { Category } from '../../models/category';

@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrl: './dashboard-admin.component.scss'
})
export class DashboardAdminComponent {
  currentUrl: string = '/dashboard_admin/categories/';
  parentUrl: string = '';
  childUrl: string = '';
  toggleLeftBar: boolean = true;
  managements: leftBar[] = [
    {
      parent: {url: "/dashboard_admin/categories", title: "Quản lý danh mục", icon: "fa-solid fa-cubes"},
      childs: [
        {url: "/dashboard_admin/categories", title: "Tất cả", icon: "fa-solid fa-cubes"},
        {url: "/dashboard_admin/categories/create", title: "Thêm mới", icon: "fa-solid fa-cubes"},
      ]
    }, {
      parent: {url: "/dashboard_admin/shops", title: "Quản lý cửa hàng", icon: "fa-solid fa-shop"},
      childs: [
        {url: "/dashboard_admin/shops", title: "Tất cả", icon: "fa-solid fa-shop"},
        {url: "/dashboard_admin/shops/create", title: "Thêm mới", icon: "fa-solid fa-shop"},
      ]
    }, {
      parent: {url: "/dashboard_admin/users", title: "Quản lý tài khoản", icon: "fa-solid fa-users"},
      childs: [
        {url: "/dashboard_admin/users", title: "Tất cả", icon: "fa-solid fa-users"},
        {url: "/dashboard_admin/users/create", title: "Thêm mới", icon: "fa-solid fa-users"},
      ]
    }, {
      parent: {url: "/dashboard_admin/statistics", title: "Thống kê", icon: "fa-solid fa-chart-line"},
      childs: [
        {url: "/dashboard_admin/statistics/1", title: "Thống kê 1", icon: "fa-solid fa-chart-line"},
        {url: "/dashboard_admin/statistics/2", title: "Thống kê 2", icon: "fa-solid fa-chart-line"},
        {url: "/dashboard_admin/statistics/3", title: "Thống kê 3", icon: "fa-solid fa-chart-line"},
      ]
    }
  ];
  categories: Category[] = [];

  constructor (private router: Router, private categoryService: CategoryService) {

  }

  ngOnInit () {
    this.initLeftBar();
    this.getCategories();
  }

  initLeftBar () : void {
    this.currentUrl = this.router.url;
    var splitUrl: string [] = this.currentUrl.split("/");
    var count = 0;
    splitUrl.forEach(i => {
      if (count < 3) {
        this.parentUrl = this.parentUrl + i + "/";
        count++;
      }
    });
    this.parentUrl = this.parentUrl.slice(0,this.parentUrl.lastIndexOf("/"));
  }

  doToggleLeftBar () : void {
    this.toggleLeftBar = !this.toggleLeftBar;
  }

  getCategories () : void {
    this.categoryService.getCategories().subscribe({
      next: (response: Category[]) => {
        this.categories = response;
      },
      complete: () => {

      },
      error: (error: any) => {
        alert(error.error.message);
      }
    });
  }
}

interface navItem {
  url: string,
  title: string,
  icon: string,
}

interface leftBar {
  parent: navItem,
  childs: navItem[],
}