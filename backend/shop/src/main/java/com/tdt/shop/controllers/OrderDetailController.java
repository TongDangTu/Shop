package com.tdt.shop.controllers;

import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.responses.OrderDetailResponse;
import com.tdt.shop.services.OrderDetailService;
import com.tdt.shop.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
  private final OrderDetailService orderDetailService;
  @PostMapping("")
  public ResponseEntity<?> createOrderDetail (
    @RequestBody @Valid OrderDetailDTO orderDetailDTO
  ) {
    try {
      OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderDetail (
    @Valid @PathVariable("id") Long id
  ) {
    try {
      OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // Lấy các order details của 1 order
  @GetMapping("/order/{orderId}")
  public ResponseEntity<?> getOrderDetails (
    @Valid @PathVariable("orderId") Long orderId
  ) {
    try {
      List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
      List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
        .map(orderDetail -> OrderDetailResponse.fromOrderDetail(orderDetail))
        .toList();
      return ResponseEntity.ok(orderDetailResponses);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrderDetail (
    @Valid @PathVariable("id") Long id,
    @RequestBody OrderDetailDTO orderDetailDTO
  ) {
    try {
      OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
      OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetail(orderDetail);
      return ResponseEntity.ok(orderDetailResponse);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderDetail (
    @Valid @PathVariable("id") Long id
  ) {
    try {
      orderDetailService.deleteOrderDetail(id);
      return ResponseEntity.ok("Deleted order detail with id:"+ id);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
