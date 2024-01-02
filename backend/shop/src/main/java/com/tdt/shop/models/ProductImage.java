package com.tdt.shop.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImage {
  public static final int MAXIMUM_IMAGES = 5; // Hằng số không ánh xạ database được. Nên chỉ là 1 hằng số trong mã nguồn
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "image_url", length = 300)
  private String imageUrl;
}
