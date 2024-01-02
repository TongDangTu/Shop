package com.tdt.shop.responses;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
  private List<ProductResponse> products;
  private int totalPages;
}
