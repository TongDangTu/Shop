import { Component } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { OrderDetailAndProductThumbnailResponse as OrderDetailAndProductNameThumbnailResponse, OrderResponse } from '../../responses/order/order.reponse';
import { OrderDetailService } from '../../services/order-detail/order-detail.service';
import { OrderService } from '../../services/order/order.service';
import { ProductService } from '../../services/product/product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.scss'
})
export class OrderDetailComponent {
  orderResponse: OrderResponse = {
    id: 0,
    user_id: 0,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    order_date: '',
    status: '',
    total_money: 0,
    shipping_method: '',
    shipping_address: '',
    shipping_date: '',
    tracking_number: '',
    payment_method: '',
    active: true,
  };

  orderDetailAndProductNameThumbnailResponses: OrderDetailAndProductNameThumbnailResponse[] = []

  shippingMethods: {value: string, text: string }[]= [
    {value: "express", text: "Chuyển phát nhanh"},
    {value: "standard", text: "Tiết kiệm"},
    {value: "priority", text: "Hỏa tốc"},
  ];

  paymentMethods: {value: string, text: string }[]= [
    {value: "cod", text: "Thanh toán khi nhận hàng"},
    {value: "atm", text: "Thẻ ngân hàng (chưa hoạt động)"},
  ];

  constructor (
    private productService: ProductService,
    private orderService: OrderService,
    private orderDetailService: OrderDetailService,
    private activeRoute: ActivatedRoute,
  ) {
  
  }

  ngOnInit(): void {
    const orderId: Number = Number(this.activeRoute.snapshot.paramMap.get("order_id"))??0;
    if (orderId != 0) {
      this.orderService.getOrder(orderId).subscribe({
        next: (response) => {
          this.orderResponse = response;
          console.log("this.orderResponse", this.orderResponse);
        },
        complete: () => {
          this.getOrderAndOrderDetailListAndProductThumbnail();
        },
        error: (error) => {
          alert(error.error.message);
        }
      });
    }
  }

  getOrderAndOrderDetailListAndProductThumbnail () {
    this.orderService.getOrderAndOrderDetailListAndProductThumbnail(this.orderResponse.id).subscribe({
      next: (response) => {
        console.log("response", response);
        this.orderResponse = {
          id: response.id,
          user_id: response.user_id,
          fullname: response.fullname,
          email: response.email,
          phone_number: response.phone_number,
          address: response.address,
          note: response.note,
          order_date: response.order_date,
          status: response.status,
          total_money: response.total_money,
          shipping_method: response.shipping_method,
          shipping_address: response.shipping_address,
          shipping_date: response.shipping_date,
          tracking_number: response.tracking_number,
          payment_method: response.payment_method,
          active: response.active,
        };
        this.orderDetailAndProductNameThumbnailResponses = response.order_detail_and_product_thumbnail_responses;
        this.orderDetailAndProductNameThumbnailResponses.forEach(orderDetailAndProductThumbnailResponse => {
          orderDetailAndProductThumbnailResponse.thumbnail = orderDetailAndProductThumbnailResponse.thumbnail ? `${envipronment.apiPrefix}/products/images/${orderDetailAndProductThumbnailResponse.thumbnail}` : '../../../assets/images/image-not-found-icon.png';
        })

        for (let i = 0; i < this.shippingMethods.length; i++) {
          if (this.orderResponse.shipping_method == this.shippingMethods[i].value) {
            this.orderResponse.shipping_method = this.shippingMethods[i].text;
            break;
          }
        }
        for (let i = 0; i < this.paymentMethods.length; i++) {
          if (this.orderResponse.payment_method == this.paymentMethods[i].value) {
            this.orderResponse.payment_method = this.paymentMethods[i].text;
            break;
          }
        }
      },
      complete: () => {

      },
      error: (error) => {
        alert(error.error.message);
      }
    });
  }
}
