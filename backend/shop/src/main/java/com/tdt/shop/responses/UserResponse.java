package com.tdt.shop.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdt.shop.models.Role;
import com.tdt.shop.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private Long id;

  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("phone_number")
  private String phoneNumber;

  private String address;

  @JsonProperty("is_active")
  private boolean isActive;

  @JsonProperty("date_of_birth")
  private Date dateOfBirth;

  @JsonProperty("facebook_account_id")
  private int facebookAccountId;

  @JsonProperty("goole_account_id")
  private int googleAccountId;

  @JsonProperty("role_name")
  private String roleName;

  public static UserResponse fromUser (User user) {
    return UserResponse.builder()
      .id(user.getId())
      .fullName(user.getFullName())
      .phoneNumber(user.getPhoneNumber())
      .address(user.getAddress())
      .isActive(user.isActive())
      .dateOfBirth(user.getDateOfBirth())
      .facebookAccountId(user.getFacebookAccountId())
      .googleAccountId(user.getGoogleAccountId())
      .roleName(user.getRole().getName())
      .build();
  }
}
