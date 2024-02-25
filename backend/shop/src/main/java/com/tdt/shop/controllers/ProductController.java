package com.tdt.shop.controllers;

import com.github.javafaker.Faker;
import com.tdt.shop.dtos.ProductDTO;
import com.tdt.shop.dtos.ProductImageDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.UniqueDataExistedException;
import com.tdt.shop.models.Product;
import com.tdt.shop.models.ProductImage;
import com.tdt.shop.responses.MessageResponse;
import com.tdt.shop.responses.ProductImageResponse;
import com.tdt.shop.responses.ProductListResponse;
import com.tdt.shop.responses.ProductResponse;
import com.tdt.shop.services.IProductService;
import com.tdt.shop.services.ProductImageService;
import com.tdt.shop.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
//@Validated
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
  private final ProductService productService;
  private final ProductImageService productImageService;
  @GetMapping("")
  public ResponseEntity<ProductListResponse> getProducts (
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "limit", defaultValue = "10") int limit,
    @RequestParam(name = "search", defaultValue = "") String search,
    @RequestParam(name = "category_id", defaultValue = "0") Long categoryId
  ) {
    // Tạo Pageable
    PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
//    Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
    Page<ProductResponse> productPage = productService.search(search, categoryId, pageRequest);
    // Lấy tổng số trang
    int totalPages = productPage.getTotalPages();
    List<ProductResponse> products = productPage.getContent();

    return ResponseEntity.ok(ProductListResponse.builder()
        .products(products)
        .totalPages(totalPages)
        .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById (@PathVariable("id") Long productId) {
      try {
        Product existingProduct = productService.getProductById(productId);
        return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
      } catch (DataNotFoundException e) {
        return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
      }
  }

  @PostMapping("")
  public ResponseEntity<?> createProduct (
    @RequestBody @Valid ProductDTO productDTO,
//    @ModelAttribute("files") List<MultipartFile> files,
    BindingResult result
  ) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(FieldError::getDefaultMessage)
          .toList();
        return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
      }

      Product product = productService.createProduct(productDTO);
      return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadImages (
    @PathVariable("id") Long productId,
    @ModelAttribute("files") List<MultipartFile> files
  ) {
    try {
      Product existingProduct = productService.getProductById(productId);

      if (files == null) {
        files = new ArrayList<>();
      }
      // Kiểm tra số lượng file đầu vào
      if (files.size() > ProductImage.MAXIMUM_IMAGES) {
        return ResponseEntity.badRequest().body(new MessageResponse("Bạn chỉ được tải lên tối đa "+ ProductImage.MAXIMUM_IMAGES +" ảnh"));
      }
      List<ProductImage> productImages = new ArrayList<>();
      for (MultipartFile file : files) {
        // Nếu có upload file thì kiểm tra tính hợp lệ
        if (file != null) {
          // Kiểm tra kích thước
          if (file.getSize() == 0) {
            continue;
          }
          long sizeOfFile = file.getSize();
          if (file.getSize() > 10 * 1024 * 1024) {   // Kích thước > 10MB
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(new MessageResponse("File quá lớn, tối đa 10MB"));
          }
          // Kiểm tra định dạng file: ảnh hay văn bản hay PDF, vân vân, ... Chỗ này chưa kiểm tra chính xác từng đuôi mở rộng. Nếu muốn, ta có thể dùng thêm .endsWith(".png")
          // Ví dụ
          // File ảnh JPEG | Đầu vào: "sample.jpg" | Đầu ra: "image/jpeg"
          // File văn bản | Đầu vào: "document.txt" | Đầu ra: "text/plain"
          // File PDF | Đầu vào: "presentation.pdf" | Đầu ra: "application/pdf"
          // File không xác định hoặc loại nội dung không được hỗ trợ: | Đầu vào: "unknown.xyz" | Đầu ra: null hoặc "application/octet-stream" (định dạng mặc định cho loại nội dung không xác định)
          String contentType = file.getContentType();
          if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File phải là ảnh");
          }
          // Lưu file và cập nhật thumbnail trong DTO
          String filename = storeFile(file);
          // Lưu vào bảng product_images
          ProductImage productImage = productService.createProductImage(
            existingProduct.getId(),
            ProductImageDTO.builder().imageUrl(filename).build()
          );
          productImages.add(productImage);
        }
      }
      return ResponseEntity.ok().body(productImages);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  private String storeFile (MultipartFile file) throws IOException {
    if (!isImageFile(file) || file.getOriginalFilename() == null) {
      throw new IOException("File phải là ảnh");
    }
    else {
      String filename = StringUtils.cleanPath(file.getOriginalFilename());
      // Sử dụng thư viện UUID để tạo ra tên file duy nhất
      String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
      // Đường dẫn thư mục muốn lưu file
      Path uploadDir = Paths.get("uploads");
      // Kiểm tra và tạo thư mục nếu không tồn tại
      if (!Files.exists(uploadDir)) {
        Files.createDirectories(uploadDir);
      }
      // Đường dẫn đầy dủ đến file
      Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
      // Sao chép file vào thư mục đích, nếu file đã tồn tại thì replace
      Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
      return uniqueFileName;
    }
  }

  private boolean isImageFile (MultipartFile file) {
    String contentType = file.getContentType();
    return contentType != null && contentType.startsWith("image/");
  }

//  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//  public ResponseEntity<String> uploadImage (
//    @ModelAttribute ProductDTO productDTO,
//    @RequestPart("file") MultipartFile file,
//    BindingResult result
//  ){
//    if (result.hasErrors()) {
//      List<String> errorMessage = result.getFieldErrors()
//        .stream()
//        .map(FieldError::getDefaultMessage)
//        .toList();
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
//    }
//    try {
//      if (file != null) {
//        if (file.getSize() > 10*1024*1024) {
//          return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("The size of the file is too large. It must be less than 10MB");
//        }
//        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
//          return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("The content type is unsupported. The uploaded file must be an image");
//        }
//        String filename = storeFile(file);
//        return ResponseEntity.ok("The file has been successfully uploaded. Name: "+ file.getOriginalFilename() +". Size: "+ file.getSize());
//      }
//      else {
//        return ResponseEntity.badRequest().body("No file has been uploaded");
//      }
//    }
//    catch (Exception e) {
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errors is happening");
//    }
//  }

//  private String storeFile (MultipartFile file) throws IOException {
//    String filename = file.getOriginalFilename();
//    String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
//    Path uploadDir = Paths.get("upload");
//    if (!Files.exists(uploadDir)) {
//      Files.createDirectories(uploadDir);
//    }
//    Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
//    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//    return uniqueFileName;
//  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateProduct (
    @PathVariable("id") long id,
    @RequestBody ProductDTO productDTO
  ) {
      try {
        Product product = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ProductResponse.fromProduct(product));
      } catch (DataNotFoundException e) {
        return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
      }

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteProduct (@PathVariable("id") long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.ok("Xóa thành công");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/images/{imageName}")
  public ResponseEntity<?> viewImage (
    @PathVariable String imageName
  ) {
    try {
      Path path = Paths.get("uploads/"+ imageName);
      UrlResource resource = new UrlResource(path.toUri());
      if (resource.exists()) {
        return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .body(resource);
      }
      else {
        return ResponseEntity.badRequest().body("URL ảnh không tồn tại");
      }
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/{id}/images")
  public ResponseEntity<?> getProductImagesByProductId (
    @PathVariable Long id
  ) {
    try {
      List<ProductImage> productImages = productImageService.getProductImageByProductId(id);
      List<ProductImageResponse> productImageResponses = productImages.stream()
        .map(productImage -> ProductImageResponse.fromProductImage(productImage))
        .toList();
      return ResponseEntity.ok(productImageResponses);
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("/ids")
  public ResponseEntity<?> getProductsByIds (
    @RequestBody() List<Long> ids
  ) {
    try {
      List<Product> products = productService.getProductsByProductIds(ids);
      List<ProductResponse> productResponses = products.stream()
        .map(product -> ProductResponse.fromProduct(product))
        .toList();
      return ResponseEntity.ok(productResponses);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @PostMapping("generateFakeProducts")
  public ResponseEntity<String> gerenateFakeProducts () {
    Faker faker = new Faker();
    for (int i = 0; i < 10000; i++) {
      String productName = faker.commerce().productName();
        try {
            if (productService.existByName(productName)) {
              continue;
            }
        } catch (UniqueDataExistedException e) {
            throw new RuntimeException(e);
        }
        ProductDTO productDTO = ProductDTO.builder()
        .name(productName)
        .price((float)faker.number().numberBetween(0, 90_000_001))
        .thumbnail("")
        .description(faker.lorem().sentence())
        .categoryId((long)faker.number().numberBetween(2, 5))
        .build();
        try {
          productService.createProduct(productDTO);
        } catch (Exception e) {
          return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    return ResponseEntity.ok("Fake Products created successfully");
  }
}
