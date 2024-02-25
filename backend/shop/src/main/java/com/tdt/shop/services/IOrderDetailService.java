package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDetailDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
  List<OrderDetail> getOrderDetailsByOrderId(Long orderId) throws DataNotFoundException;
  OrderDetail getOrderDetail (Long id) throws DataNotFoundException;
  OrderDetail createOrderDetail (OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  OrderDetail updateOrderDetail (Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  void deleteOrderDetail (Long id) throws DataNotFoundException;

  List<OrderDetail> createOrderDetails (List<OrderDetailDTO> orderDetailDTOs) throws DataNotFoundException;
}
