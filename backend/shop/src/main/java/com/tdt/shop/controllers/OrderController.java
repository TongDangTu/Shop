package com.tdt.shop.controllers;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.responses.OrderAndOrderDetailListAndProductThumbnailResponse;
import com.tdt.shop.responses.OrderResponse;
import com.tdt.shop.services.IOrderService;
import com.tdt.shop.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final OrderService orderService;

  @GetMapping("/user/{user_id}")
  public ResponseEntity<?> getOrders (
    @Valid @PathVariable("user_id") Long userId
  ){
    try {
      List<Order> orders = orderService.getOrderByUserId(userId);
      List<OrderResponse> orderResponses = orders.stream()
        .map(order -> OrderResponse.fromOrder(order))
        .toList();
      return ResponseEntity.ok(orderResponses);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrder (
    @Valid @PathVariable("id") Long orderId
  ){
    try {
      Order order = orderService.getOrder(orderId);
      return ResponseEntity.ok(OrderResponse.fromOrder(order));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("")
  public ResponseEntity<?> createOrder (
    @RequestBody @Valid OrderDTO orderDTO,
    BindingResult result
    ) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
      }
      Order order = orderService.createOrder(orderDTO);
      return ResponseEntity.ok(OrderResponse.fromOrder(order));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder (
    @Valid @PathVariable long id,
    @Valid @RequestBody OrderDTO orderDTO
  ) {
      try {
        Order order = orderService.updateOrder(id, orderDTO );
        return ResponseEntity.ok(OrderResponse.fromOrder(order));
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
      }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder (
    @Valid @PathVariable long id
  ) {
      try {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Xóa Order thành công");
      } catch (DataNotFoundException e) {
        return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
      }
  }

  @GetMapping("/{id}/order_details/products_thumbnail")
  public ResponseEntity<?> getOrderAndOrderDetailListAndProductThumbnailResponseByOrderId (
    @PathVariable Long id
  ) {
    try {
      List<OrderDetail> orderDetails = new ArrayList<>();
      List<Product> products = new ArrayList<>();
      List<Object[]> objects = orderService.getOrderAndOrderDetailListResponseByOrderId(id);
      Order order = (Order) objects.get(0)[0];
      for (Object[] object : objects) {
        OrderDetail orderDetail = (OrderDetail) object[1];
        Product product = (Product) object[2];
        orderDetails.add((orderDetail));
        products.add((product));
      }
      OrderAndOrderDetailListAndProductThumbnailResponse orderAndOrderDetailListAndProductThumbnailResponse = OrderAndOrderDetailListAndProductThumbnailResponse.fromOrderAndOrderDetailAndProduct(order, orderDetails, products);
      return ResponseEntity.ok(orderAndOrderDetailListAndProductThumbnailResponse);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }
}

