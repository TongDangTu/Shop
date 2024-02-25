import { Component } from '@angular/core';
import { CartItem } from '../../models/cart-item';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart/cart.service';
import { ProductService } from '../../services/product/product.service';
import { envipronment } from '../../environments/environment';
import { OrderDTO } from '../../dtos/order/order.dto';
import { OrderService } from '../../services/order/order.service';
import { OrderDetail } from '../../models/order.detail';
import { OrderDetailDTO } from '../../dtos/order-detail/order-detail.dto';
import { OrderResponse } from '../../responses/order/order.reponse';
import { OrderDetailService } from '../../services/order-detail/order-detail.service';
import { OrderDetailResponse } from '../../responses/order-detail/order-detail.reponse';
import { TokenService } from '../../services/token/token.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {
  orderForm!: FormGroup;

  cart: CartItem[] = [];
  products: Product[] = [];
  detailCarts: {cart: CartItem, product: Product, amount: number}[] = [];

  shippingMethods: {value: string, text: string }[]= [
    {value: "express", text: "Chuyển phát nhanh"},
    {value: "standard", text: "Tiết kiệm"},
    {value: "priority", text: "Hỏa tốc"},
  ];

  paymentMethods: {value: string, text: string }[]= [
    {value: "cod", text: "Thanh toán khi nhận hàng"},
    {value: "atm", text: "Thẻ ngân hàng (chưa hoạt động)"},
  ];

  orderDTO: OrderDTO = {
    user_id: 0,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    shipping_method: 'express',
    shipping_address: '',
    shipping_date: '',
    payment_method: 'cod',
  }

  constructor (
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private orderDetailService: OrderDetailService,
    private tokenService: TokenService,
    private formBuilder: FormBuilder,
    private router: Router,
    ) {
    this.orderForm = this.formBuilder.group({
      fullname: ['Tống Đăng Tú', [Validators.required]],
      email: ['tongdangtu@gmail.com', [Validators.email]],
      phone_number: ['0010102002', [Validators.required, Validators.minLength(9), Validators.maxLength(10)]],
      address: ['Thanh Hóa', [Validators.required]],
      note: [''],
      shipping_method: ['express'],
      payment_method: ['cod'],
    });
  }

  ngOnInit(): void {
    this.orderDTO.user_id = this.tokenService.getUserId();
    console.log("this.tokenService.getUserId()", this.tokenService.getUserId());
    this.getCart();
    this.getProducts();
  }

  getCart () {
    this.cart = this.cartService.getCart();
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
        this.orderDTO.total_money = this.orderDTO.total_money + amount;
      }
    }
  }

  createOrder () {
    this.getDataForm();

    if (this.orderForm.valid) {
      let orderId: number;
      this.orderService.createOrder(this.orderDTO).subscribe({
        next: (response: OrderResponse) => {
          orderId = response.id;
        },
        complete: () => {
          if (orderId) {
            let orderDetailDTOs: OrderDetailDTO[] = [];
  
            this.detailCarts.forEach((detailCart) => {
              let orderDetailDTO: OrderDetailDTO = {
                order_id: orderId,
                product_id: detailCart.cart.productId,
                price: detailCart.amount,
                numbers_of_products: detailCart.cart.quantity,
                total_money: detailCart.amount,
              };
              orderDetailDTOs.push(orderDetailDTO);
            });
  
            this.createDetails(orderDetailDTOs);
          }
        },
        error: (error: any) => {
          alert(error.error.message);
        }
      });
    }
    else {
      alert("Chưa điền dủ thông tin");
    }
  }

  createDetails (orderDetailDTOs: OrderDetailDTO[]) {
    this.orderDetailService.createOrderDetails(orderDetailDTOs).subscribe({
      next: (response: OrderDetailResponse[]) => {
        
      },
      complete: () => {
        alert("Đặt hàng thành công");
        this.cartService.removeCart();
        this.router.navigate([`/`])
      },
      error: (error: any) => {
        alert(error.error.message);
      }
    });
  }

  getDataForm () {
    this.orderDTO.fullname = this.orderForm.get('fullname')!.value;
    this.orderDTO.email = this.orderForm.get('email')!.value;
    this.orderDTO.phone_number = this.orderForm.get('phone_number')!.value;
    this.orderDTO.address = this.orderForm.get('address')!.value;
    this.orderDTO.note = this.orderForm.get('note')!.value;
    this.orderDTO.shipping_method = this.orderForm.get('shipping_method')!.value;
    this.orderDTO.payment_method = this.orderForm.get('payment_method')!.value;
  }
}
