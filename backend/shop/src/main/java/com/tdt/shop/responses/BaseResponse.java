package com.tdt.shop.responses;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseResponse {
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // Thêm @PrePersist và @PreUpdate để createdAt và updatedAt tự cập nhật
  // Thực hiện các hành động trước khi Entity được thêm vào CSDL
  @PrePersist
  protected void onCreated() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  // Thực hiện các hành động trước khi Entity được cập nhật
  @PreUpdate
  protected void onUpdate () {
    updatedAt = LocalDateTime.now();
  }
}
