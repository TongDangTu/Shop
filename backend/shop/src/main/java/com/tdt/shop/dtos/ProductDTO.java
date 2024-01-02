package com.tdt.shop.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  // Kiểm tra Không trống
  @NotBlank(message = "Name is required")
  // Kiểm tra Kích thước
  @Size(min = 3, max = 200, message = "Name must be between 3 and 300 characters")
  private String name;
  // Kiểm tra Min Max
  @Min(value = 0, message = "Price must be greater than or equal to 0")
  @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
  private Float price;
  private String thumbnail;
  private String description;
  @JsonProperty("category_id")
  private Long categoryId;
}
