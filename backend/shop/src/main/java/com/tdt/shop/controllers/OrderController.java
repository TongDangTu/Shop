package com.tdt.shop.controllers;

import com.tdt.shop.dtos.OrderDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Order;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.responses.OrderResponse;
import com.tdt.shop.services.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final IOrderService orderService;

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
}

