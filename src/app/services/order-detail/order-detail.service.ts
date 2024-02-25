import { Injectable } from '@angular/core';
import { envipronment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../models/order';
import { OrderDetailResponse } from '../../responses/order-detail/order-detail.reponse';
import { OrderDTO } from '../../dtos/order/order.dto';
import { OrderDetailDTO } from '../../dtos/order-detail/order-detail.dto';

@Injectable({
  providedIn: 'root'
})
export class OrderDetailService {
  orderDetailUrl = `${envipronment.apiPrefix}/order_details`;
  httpHeaders = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
      "Add-Token": "true"
    })
  }
  constructor(private httpClient: HttpClient) {

  }

  createOrderDetails (orderDetailDTOs: OrderDetailDTO[]) : Observable<OrderDetailResponse[]> {
    const url = `/list`;
    return this.httpClient.post<OrderDetailResponse[]>(`${this.orderDetailUrl}${url}`, orderDetailDTOs, this.httpHeaders);
  }
}
