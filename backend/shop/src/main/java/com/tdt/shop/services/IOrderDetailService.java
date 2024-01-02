package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDetailDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
  OrderDetail createOrderDetail (OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  OrderDetail getOrderDetail (Long id) throws DataNotFoundException;
  OrderDetail updateOrderDetail (Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  void deleteOrderDetail (Long id);
  List<OrderDetail> findByOrderId (Long orderId) throws DataNotFoundException;
}
