import { Injectable } from '@angular/core';
import { CartItem } from '../../models/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  cart: CartItem[] = [];

  constructor() {
    
  }

  showCart () {
    console.log(this.cart);
  }

  getCart () : CartItem[] {
    const cartLocalStorage = localStorage.getItem('cart');
    if (cartLocalStorage != null) {
      this.cart = JSON.parse(cartLocalStorage);
    }
    return this.cart;
  }

  addToCart (cartItem: CartItem) {
    if (this.cart.length <= 0 ) {
      this.cart.push(cartItem);
    }
    else {
      let isNew = true;
      this.cart.forEach(cartItemExisting => {
        if (cartItemExisting.productId == cartItem.productId) {
          cartItemExisting.quantity = cartItemExisting.quantity + cartItem.quantity;
          isNew = false;
        }
      })
      if (isNew) {
        this.cart.push(cartItem);
      }
    }
    this.saveCart();
  }

  private saveCart () {
    localStorage.setItem('cart', JSON.stringify(this.cart));
  }

  removeCart () {
    this.cart = [];
    localStorage.removeItem('cart');
  }
}
