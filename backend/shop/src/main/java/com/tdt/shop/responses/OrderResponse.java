package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import com.tdt.shop.models.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("fullname")
  private String fullName;

  private String email;

  @JsonProperty("phone_number")
  private String phoneNumber;

  private String address;

  private String note;

  @JsonProperty("order_date")
  private LocalDate orderDate;

  private String status;

  @JsonProperty("total_money")
  private Float totalMoney;

  @JsonProperty("shipping_method")
  private String shippingMethod;

  @JsonProperty("shipping_address")
  private String shippingAddress;

  @JsonProperty("shipping_date")
  private LocalDate shippingDate;

  @JsonProperty("tracking_number")
  private String trackingNumber;

  @JsonProperty("payment_method")
  private String paymentMethod;

  private Boolean active;

  public static OrderResponse fromOrder (Order order) {
    return OrderResponse.builder()
    .id(order.getId())
    .userId(order.getUser().getId())
    .fullName(order.getFullName())
    .email(order.getEmail())
    .phoneNumber(order.getPhoneNumber())
    .address(order.getAddress())
    .note(order.getNote())
    .orderDate(order.getOrderDate())
    .status(order.getStatus())
    .totalMoney(order.getTotalMoney())
    .shippingMethod(order.getShippingMethod())
    .shippingAddress(order.getShippingAddress())
    .shippingDate(order.getShippingDate())
    .trackingNumber(order.getTrackingNumber())
    .paymentMethod(order.getPaymentMethod())
    .active(order.getActive())
    .build();
  }
}
