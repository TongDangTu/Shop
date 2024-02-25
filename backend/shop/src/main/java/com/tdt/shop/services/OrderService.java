package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.InvalidParamException;
import com.tdt.shop.exceptions.PermissionDenyException;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderStatus;
import com.tdt.shop.models.User;
import com.tdt.shop.repositories.OrderRepository;
import com.tdt.shop.repositories.UserRepository;
import com.tdt.shop.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  @Override
  public List<Order> getAllOrder() {
    return orderRepository.findAll();
  }

  @Override
  public List<Order> getOrderByUserId(Long userId) throws DataNotFoundException {
    userRepository.findById(userId)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user có id: "+ userId));
    return orderRepository.findByUserId(userId);
  }

  @Override
  public Order getOrder (Long id) throws DataNotFoundException {
    return orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
  }

  @Override
  @Transactional
  public Order createOrder(OrderDTO orderDTO) throws InvalidParamException {
    // kiểm tra xem user_id có tồn tại hay không
    User user= userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DateTimeException("Không tồn tại người dùng có id: "+ orderDTO.getUserId()));
    LocalDate shippingDate;
    // Kiểm tra shipping date phải ≥ ngày hôm nay
    if (orderDTO.getShippingDate() == null) {
      shippingDate = LocalDate.now();
    }
    else {
      shippingDate = orderDTO.getShippingDate();
    }
    if (shippingDate.isBefore(LocalDate.now())) {
      throw new InvalidParamException("Ngày giao hàng không được giao trước hôm nay");
    }

    Order order = Order.builder()
//      .id(orderDTO.getId())
      .user(user)
      .fullName(orderDTO.getFullName())
      .email(orderDTO.getEmail())
      .phoneNumber(orderDTO.getPhoneNumber())
      .address(orderDTO.getAddress())
      .note(orderDTO.getNote())
      .orderDate(LocalDate.now())
      .status(OrderStatus.PENDING)
      .totalMoney(orderDTO.getTotalMoney())
      .shippingMethod(orderDTO.getShippingMethod())
      .shippingAddress(orderDTO.getShippingAddress())
      .shippingDate(shippingDate)
//      .trackingNumber(orderDTO.getTrackingNumber())
      .paymentMethod(orderDTO.getPaymentMethod())
      .active(true)
      .build();

    return orderRepository.save(order);
  }

  @Override
  @Transactional
  public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException, InvalidParamException {
    Order existingOrder = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    User existingUser = userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có id: "+ orderDTO.getUserId()));
    LocalDate shippingDate;
    // Kiểm tra shipping date phải ≥ ngày hôm nay
    if (orderDTO.getShippingDate() == null) {
      shippingDate = LocalDate.now();
    }
    else {
      shippingDate = orderDTO.getShippingDate();
    }
    if (shippingDate.isBefore(LocalDate.now())) {
      throw new InvalidParamException("Ngày giao hàng không được giao trước hôm nay");
    }
    existingOrder.setFullName(orderDTO.getFullName());
    existingOrder.setEmail(orderDTO.getEmail());
    existingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
    existingOrder.setAddress(orderDTO.getAddress());
    existingOrder.setNote(orderDTO.getNote());
    existingOrder.setOrderDate(LocalDate.now());
//    existingOrder.setStatus(orderDTO.getStatus());
    existingOrder.setTotalMoney(orderDTO.getTotalMoney());
    existingOrder.setShippingMethod(orderDTO.getShippingMethod());
    existingOrder.setShippingAddress(orderDTO.getShippingAddress());
    existingOrder.setShippingDate(shippingDate);
//    existingOrder.setTrackingNumber(orderDTO.getTrackingNumber());
    existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
//    existingOrder.setActive(orderDTO.getActive());
    return orderRepository.save(existingOrder);
  }

  @Override
  @Transactional
  public Order updateOrderForUser(Long id, OrderDTO orderDTO) throws DataNotFoundException, PermissionDenyException {
    Order existingOrder = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    User existingUser = userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có id: "+ orderDTO.getUserId()));

    existingOrder.setFullName(orderDTO.getFullName());
    existingOrder.setEmail(orderDTO.getEmail());
    existingOrder.setPhoneNumber(orderDTO.getPhoneNumber());
    existingOrder.setAddress(orderDTO.getAddress());
    existingOrder.setNote(orderDTO.getNote());
    existingOrder.setOrderDate(LocalDate.now());
    existingOrder.setTotalMoney(orderDTO.getTotalMoney());
    existingOrder.setShippingMethod(orderDTO.getShippingMethod());
    existingOrder.setShippingAddress(orderDTO.getShippingAddress());
//    existingOrder.setTrackingNumber(orderDTO.getTrackingNumber());
    existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
//    existingOrder.setActive(orderDTO.getActive());
    return orderRepository.save(existingOrder);
  }

  @Override
  @Transactional
  public Order updateOrderForAdmin(Long id, OrderDTO orderDTO) throws DataNotFoundException, InvalidParamException {
    Order existingOrder = orderRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng"));
    User existingUser = userRepository.findById(orderDTO.getUserId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng có id: "+ orderDTO.getUserId()));
    LocalDate shippingDate;
    // Kiểm tra shipping date phải ≥ ngày hôm nay
    if (orderDTO.getShippingDate() == null) {
      shippingDate = LocalDate.now();
    }
    else {
      shippingDate = orderDTO.getShippingDate();
    }
    if (shippingDate.isBefore(LocalDate.now())) {
      throw new InvalidParamException("Ngày giao hàng không được giao trước hôm nay");
    }
//    existingOrder.setStatus(orderDTO.getStatus());
    existingOrder.setShippingDate(shippingDate);
//    existingOrder.setTrackingNumber(orderDTO.getTrackingNumber());
//    existingOrder.setActive(orderDTO.getActive());
    return orderRepository.save(existingOrder);
  }

  @Override
  @Transactional
  public void deleteOrder(Long id) throws DataNotFoundException {
    Order order = getOrder(id);
    order.setActive(false);
    orderRepository.save(order);
  }

  @Override
  public List<Object[]> getOrderAndOrderDetailListResponseByOrderId (Long orderId) {
    return orderRepository.getOrderAndOrderDetailListResponseByOrderId(orderId);
  }
}
