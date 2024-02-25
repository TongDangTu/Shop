import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';

import { AppRoutingModule } from './app-routing.module';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { OrderComponent } from './components/order/order.component';
import { CartComponent } from './components/cart/cart.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { AppComponent } from './app.component';
import { DashboardAdminComponent } from './components/dashboard-admin/dashboard-admin.component';

@NgModule({
  declarations: [
    HomeComponent,  
    HeaderComponent,
    FooterComponent,
    OrderComponent,
    CartComponent,
    LoginComponent,
    RegisterComponent,
    ProductDetailComponent,
    OrderDetailComponent,
    AppComponent,
    DashboardAdminComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: TokenInterceptor,
    multi: true,

  }],
  bootstrap: [
    AppComponent
    // RegisterComponent,
    // LoginComponent,
    // HomeComponent,
    // ProductDetailComponent,
    // CartComponent,
    // OrderComponent,
    // OrderDetailComponent,
  ]
})
export class AppModule { }
