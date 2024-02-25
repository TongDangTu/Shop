import { Injectable } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OrderDTO } from '../../dtos/order/order.dto';
import { OrderResponse } from '../../responses/order/order.reponse';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  orderUrl = `${envipronment.apiPrefix}/orders`;
  httpHeaders = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      "Add-Token": "true"
    })
  }
  constructor(private httpClient: HttpClient) {

  }

  getOrder (id: Number) : Observable<OrderResponse> {
    const url = `/${id}`;
    return this.httpClient.get<OrderResponse>(`${this.orderUrl}${url}`,this.httpHeaders);
  }
 
  createOrder (orderDTO: OrderDTO) : Observable<OrderResponse> {
    const url = ``;
    return this.httpClient.post<OrderResponse>(`${this.orderUrl}${url}`, orderDTO, this.httpHeaders);
  }

  getOrderAndOrderDetailListAndProductThumbnail (id: Number) : Observable<any> {
    console.log("getOrderAndOrderDetailListAndProductThumbnail");
    const url = `/${id}/order_details/products_thumbnail`;
    return this.httpClient.get<any>(`${this.orderUrl}${url}`,this.httpHeaders);
  }
}
