import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../models/product';
import { HttpClient } from '@angular/common/http';
import { envipronment } from '../../environments/environment';
import { ProductImage } from '../../models/product-image';
import { CartService } from '../../services/cart/cart.service';
import { CartItem } from '../../models/cart-item';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  productImages: ProductImage[] = [];

  product: Product = {
    id: 0,
    name: '',
    price: 0,
    thumbnail: '',
    description: '',
    category_id: 0,
    createdAt: '',
    updatedAt: ''
  };

  quantity: number = 1;

  currentCarouselIndex: number = 0;

  constructor (private productService: ProductService, private cartService: CartService, private activeRoute: ActivatedRoute) {
    
  }

  ngOnInit() {
    const id: number = Number(this.activeRoute.snapshot.paramMap.get('id')) || 0;
    this.getProduct(id);
    this.getProductImagesByProductId(id);
  }

  getProduct (id: number){
    this.productService.getProduct(id).subscribe({
      next: (response: Product) => {
        this.product = response;
        this.product.thumbnail = response.thumbnail ? `${envipronment.apiPrefix}/products/images/${response.thumbnail}` : '../../../assets/images/image-not-found-icon.png';
      },
      complete: () => {

      },
      error: (error) => {
        alert(error.error.message);
      }
    })
  }

  getProductImagesByProductId (id: number) {
    this.productService.getProductImagesByProductId(id).subscribe({
      next: (response: ProductImage[]) => {
        if (response.length == 0) {
          this.productImages = [{id: 0, product_id: 0, image_url: '../../../assets/images/image-not-found-icon.png'}];
        }
        else {
          response.forEach((productImage:ProductImage) => {
            productImage.image_url = productImage.image_url ? `${envipronment.apiPrefix}/products/images/${productImage.image_url}` : '../../../assets/images/image-not-found-icon.png'
          });
          this.productImages = response;
        }
        this.activeCarouselItem(0);
      },
      complete: () => {

      },
      error: (error) => {
        alert(error.error.message);
      }
    });
  }

  increase () {
    this.quantity = this.quantity + 1;
  }

  decrease () {
    this.quantity = this.quantity - 1;
  }

  activeCarouselItem (index: number) {
    if (this.product && this.productImages && this.productImages.length > 0) {
      if (index < 0) {
        index = this.productImages.length - 1;
      }
      else if (index > this.productImages.length) {
        index = 0;
      }
      this.currentCarouselIndex = index;
    }
  }

  thumbnailItemClick (index: number) {
    this.currentCarouselIndex = index;
  }

  previousCarousel() {
    this.activeCarouselItem(this.currentCarouselIndex - 1);
  }

  nextCarousel() {
    this.activeCarouselItem(this.currentCarouselIndex + 1);
  }

  addToCart() {
    const cartItem: CartItem = {
      productId: this.product.id,
      quantity: this.quantity
    };
    this.cartService.addToCart(cartItem);
  }
}
