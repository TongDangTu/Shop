package com.tdt.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
  @NotBlank(message = "Phone number is required")
  @JsonProperty("phone_number")
  private String phoneNumber;
  @NotBlank(message = "Password is required")
  private String password;
}
