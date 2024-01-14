package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.InvalidParamException;
import com.tdt.shop.exceptions.PermissionDenyException;
import com.tdt.shop.models.Order;
import com.tdt.shop.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
  List<Order> getAllOrder();
  List<Order> getOrderByUserId(Long userId) throws DataNotFoundException;
  Order getOrder (Long id) throws DataNotFoundException;
  Order createOrder (OrderDTO orderDTO) throws DataNotFoundException, InvalidParamException;
  Order updateOrder (Long id, OrderDTO orderDTO) throws DataNotFoundException, InvalidParamException;
  public Order updateOrderForUser(Long id, OrderDTO orderDTO) throws DataNotFoundException, PermissionDenyException;
  public Order updateOrderForAdmin(Long id, OrderDTO orderDTO) throws DataNotFoundException, InvalidParamException;
  void deleteOrder (Long id) throws DataNotFoundException;
}
