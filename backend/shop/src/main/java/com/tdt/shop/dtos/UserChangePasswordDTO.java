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
public class UserChangePasswordDTO {
  @NotBlank(message = "Password is required")
  @JsonProperty("password")
  private String password;

  @NotBlank(message = "New password is required")
  @JsonProperty("new_password")
  private String newPassword;

  @NotBlank(message = "Retype password is required")
  @JsonProperty("retype_password")
  private String retypePassword;
}
