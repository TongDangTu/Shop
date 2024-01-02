package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderStatus;
import com.tdt.shop.models.User;
import com.tdt.shop.repositories.OrderRepository;
import com.tdt.shop.repositories.UserRepository;
import com.tdt.shop.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {
    // kiểm tra xem user_id có tồn tại hay không
    User user= userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DateTimeException("Không tồn tại người dùng có id: "+ orderDTO.getUserId()));

    // Tạo một luồng bằng ánh xạ riêng để kiểm soát việc ánh xạ
    modelMapper.typeMap(OrderDTO.class, Order.class)    // convert từ OrderDTO sang Order
      .addMappings(mapper -> mapper.skip(Order::setId));  // bỏ qua id
    // Câp nhật các trường của đơn hàng từ orderDTO
    Order order = new Order();
    modelMapper.map(orderDTO, order);
    order.setUser(user);
    order.setOrderDate(LocalDate.now());
    order.setStatus(OrderStatus.PENDING);
    LocalDate shippingDate;
    // Kiểm tra shipping date phải ≥ ngày hôm nay
    if (orderDTO.getShippingDate() == null) {
      shippingDate = LocalDate.now();
    }
    else {
      shippingDate = orderDTO.getShippingDate();
    }
    if (shippingDate.isBefore(LocalDate.now())) {
      throw new DataNotFoundException("Ngày giao hàng không được giao trước hôm nay");
    }

    order.setShippingDate(shippingDate);
    order.setActive(true);
    orderRepository.save(order);
    modelMapper.typeMap(Order.class, OrderResponse.class);
    OrderResponse orderResponse = new OrderResponse();
    modelMapper.map(order, orderResponse);
    return orderResponse;
  }

  @Override
  public Order getOrder (Long id) throws DataNotFoundException {
    return orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Đơn hàng có id: "+ id));
  }

  @Override
  public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Đơn hàng có id: "+ id));
    User existingUser = userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Người dùng có id: "+ orderDTO.getUserId()));
    modelMapper.typeMap(OrderDTO.class, Order.class)
      .addMappings(mapper -> mapper.skip(Order::setId));
    modelMapper.map(orderDTO, existingOrder);
    existingOrder.setUser(existingUser);
    return orderRepository.save(existingOrder);
  }

  @Override
  public void deleteOrder(Long id) throws DataNotFoundException {
    Order order = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Đơn hàng có id: "+ id));
    order.setActive(false);
    orderRepository.save(order);
  }

  @Override
  public List<Order> findByUserId(Long userId) {
    return orderRepository.findByUserId(userId);
  }
}
