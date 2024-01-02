package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
  private Long id;

  @JsonProperty("order_id")
  private Long orderId;

  @JsonProperty("product_id")
  private Long productId;

  @JsonProperty("price")
  private Float price;

  @JsonProperty("number_of_products")
  private int numberOfProducts;

  @JsonProperty("total_money")
  private Float totalMoney;

  @JsonProperty("color")
  private String color;

  public static OrderDetailResponse fromOrderDetail (OrderDetail orderDetail) {
      return OrderDetailResponse.builder()
        .id(orderDetail.getId())
        .orderId(orderDetail.getOrder().getId())
        .productId(orderDetail.getProduct().getId())
        .price(orderDetail.getPrice())
        .numberOfProducts(orderDetail.getNumberOfProducts())
        .totalMoney(orderDetail.getTotalMoney())
        .color(orderDetail.getColor())
        .build();
  }
}
