package com.tdt.shop.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data                 // Tự động tạo các phương thức toString, equals, hashCode, cùng với getter và setter cho tất cả các trường.
@Getter               // Tự động tạo phương thức getter (hoặc cho trường được đánh dấu)
@Setter               // Tự động tạo phương thức setter (hoặc cho trường được đánh dấu)
@NoArgsConstructor    // Tự động tạo một constructor không có tham số
@AllArgsConstructor   // Tự động tạo một constructor chứa tất cả các tham số của lớp
public class CategoryDTO {
  @NotEmpty(message = "Category's name cannot be empty")
  private String name;
}
