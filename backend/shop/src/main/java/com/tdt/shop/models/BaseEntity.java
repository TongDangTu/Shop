package com.tdt.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass     //
public class BaseEntity {
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // Thêm @PrePersist và @PreUpdate để createdAt và updatedAt tự cập nhật
  @PrePersist
  protected void onCreated() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate () {
    updatedAt = LocalDateTime.now();
  }
}
