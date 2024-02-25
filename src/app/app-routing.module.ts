import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { CartComponent } from './components/cart/cart.component';
import { OrderComponent } from './components/order/order.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'product_details/:id', component: ProductDetailComponent},
  {path: 'carts', component: CartComponent},
  {path: 'orders', component: OrderComponent},
  {path: 'order_details/:order_id', component: OrderDetailComponent},
  {path: 'dashboard_admin', component: DashboardAdminComponent},
  {path: 'dashboard_admin/categories', component: DashboardAdminComponent},
  {path: 'dashboard_admin/categories/create', component: DashboardAdminComponent},
  {path: 'dashboard_admin/shops', component: DashboardAdminComponent},
  {path: 'dashboard_admin/shops/create', component: DashboardAdminComponent},
  {path: 'dashboard_admin/users', component: DashboardAdminComponent},
  {path: 'dashboard_admin/users/create', component: DashboardAdminComponent},
  {path: 'dashboard_admin/statistics', component: DashboardAdminComponent},
  {path: 'dashboard_admin/statistics/1', component: DashboardAdminComponent},
  {path: 'dashboard_admin/statistics/2', component: DashboardAdminComponent},
  {path: 'dashboard_admin/statistics/3', component: DashboardAdminComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
