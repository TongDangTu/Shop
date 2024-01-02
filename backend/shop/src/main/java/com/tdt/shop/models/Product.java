package com.tdt.shop.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 350)
  private String name;

  private Float price;

  @Column(name = "thumbnail", length = 300)
  private String thumbnail;

  @Column(name = "description")
  private String description;

  @ManyToOne  // Mối quan hệ n-1 (n Product, 1 category)
  @JoinColumn(name = "category_id")   // thuộc tính này trong Product
  private Category category;          // bảng Category
}
