package com.tdt.shop.responses;

import com.tdt.shop.models.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
  private Long id;
  private Long product_id;
  private String image_url;

  public static ProductImageResponse fromProductImage (ProductImage productImage) {
    return ProductImageResponse.builder()
      .id(productImage.getId())
      .product_id(productImage.getProduct().getId())
      .image_url(productImage.getImageUrl())
      .build();
  }
}
