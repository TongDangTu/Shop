package com.tdt.shop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  @Id   // Khóa chính
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Khóa chính tự tăng lên 1
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;
}
