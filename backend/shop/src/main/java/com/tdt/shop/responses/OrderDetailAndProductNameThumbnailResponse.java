package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class OrderDetailAndProductNameThumbnailResponse extends OrderDetailResponse {
  @JsonProperty("name")
  private String name;

  @JsonProperty("thumbnail")
  private String thumbnail;

  public static OrderDetailAndProductNameThumbnailResponse fromOrderDetailAndProductNameThumbnail (OrderDetail orderDetail, Product product) {
    return OrderDetailAndProductNameThumbnailResponse.builder()
      .id(orderDetail.getId())
      .orderId(orderDetail.getOrder().getId())
      .productId(orderDetail.getProduct().getId())
      .price(orderDetail.getPrice())
      .numberOfProducts(orderDetail.getNumberOfProducts())
      .totalMoney(orderDetail.getTotalMoney())
      .name(product.getName())
      .thumbnail(product.getThumbnail())
      .build();
  }
}
