package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("token")
  private String token;
}
