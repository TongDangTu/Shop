package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Order;
import com.tdt.shop.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
  OrderResponse createOrder (OrderDTO orderDTO) throws DataNotFoundException;
  Order getOrder (Long id) throws DataNotFoundException;
  Order updateOrder (Long id, OrderDTO orderDTO) throws DataNotFoundException;
  void deleteOrder (Long id) throws DataNotFoundException;
  List<Order> findByUserId(Long userId);
}
