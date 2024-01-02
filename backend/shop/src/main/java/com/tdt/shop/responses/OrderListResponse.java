package com.tdt.shop.responses;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
  private List<OrderResponse> orders;
  private long userId;
}
