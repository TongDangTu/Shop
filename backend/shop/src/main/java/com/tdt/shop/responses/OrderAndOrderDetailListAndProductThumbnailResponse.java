package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAndOrderDetailListAndProductThumbnailResponse extends OrderResponse {
  @JsonProperty("order_detail_and_product_thumbnail_responses")
  private List<OrderDetailAndProductNameThumbnailResponse> orderDetailAndProductThumbnailResponses;
  public static OrderAndOrderDetailListAndProductThumbnailResponse fromOrderAndOrderDetailAndProduct (Order order, List<OrderDetail> orderDetails, List<Product> products) {

    List<OrderDetailAndProductNameThumbnailResponse> orderDetailAndProductThumbnailResponses = new ArrayList<>();
    for(int i = 0; i < orderDetails.size(); i++) {
      OrderDetailAndProductNameThumbnailResponse orderDetailAndProductThumbnailResponse = OrderDetailAndProductNameThumbnailResponse.fromOrderDetailAndProductNameThumbnail(orderDetails.get(i), products.get(i));
      orderDetailAndProductThumbnailResponses.add(orderDetailAndProductThumbnailResponse);
    }

    return OrderAndOrderDetailListAndProductThumbnailResponse.builder()
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
      .orderDetailAndProductThumbnailResponses(orderDetailAndProductThumbnailResponses)
      .build();
  }
}

