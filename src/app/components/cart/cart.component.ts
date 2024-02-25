import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart/cart.service';
import { CartItem } from '../../models/cart-item';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product';
import { envipronment } from '../../environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {
  cart: CartItem[] = [];
  products: Product[] = [];
  detailCarts: {cart: CartItem, product: Product, amount: number}[] = [];
  totalPrice: number = 0;
  constructor (private cartService: CartService, private productService: ProductService, private router: Router) {
    
  }

  ngOnInit(): void {
    this.getCart();
    this.getProducts();
  }

  getCart () {
    this.cart = this.cartService.getCart();
    console.log("this.cart", this.cart);
    this.getDetailCarts();
  }

  getProducts () {
    const product_ids: number[] = this.cart.map(cartItem => cartItem.productId);
    this.productService.getProductsByProductIds(product_ids).subscribe({
      next: (response: Product[]) => {
        response.forEach((product: Product) => {
          product.thumbnail = product.thumbnail ? `${envipronment.apiPrefix}/products/images/${product.thumbnail}` : '../../../assets/images/image-not-found-icon.png';
        });
        this.products = response;
      },
      complete: () => {
        this.getDetailCarts();
      },
      error: (error: any) => {
        alert(error.error.message);
      }
    });
  }

  getDetailCarts () {
    if (this.cart.length == this.products.length) {
      this.detailCarts = [];
      for (let i = 0; i < this.cart.length; i++) {
        let amount = this.cart[i].quantity * this.products[i].price;
        this.detailCarts.push({
          cart: this.cart[i],
          product: this.products[i],
          amount: amount
        });
        this.totalPrice = this.totalPrice + amount;
      }
    }
  }

  order() {
    this.router.navigate(["/orders"]);
  }
}