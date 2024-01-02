package com.tdt.shop.controllers;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.models.Category;
import com.tdt.shop.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated    // Nếu muốn xuất message khi Validate @NotEmpty thì phải bỏ @Validated. Vì @Validate kiểm tra ở mức class, không thể vào bên trong hàm được
public class CategoryController {
  private final CategoryService categoryService;

  // POST
//  @PostMapping("")
//  public ResponseEntity<String> insertCategory () {
//    return ResponseEntity.ok("Đây là insertCategory");
//  }

  // POST: với tham số là một đối tượng? Data Transfer Object = Request Object
  // Thêm tham số
  @PostMapping("")
  public ResponseEntity<?> createCategory (       // Generate ? khi không biết kiểu là gì
    @RequestBody @Valid CategoryDTO categoryDTO,
    BindingResult result            // Thêm BindingResult result khi muốn hiển thị thông báo massage từ Validation
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()   // lấy danh sách lỗi
        .stream()              // .stream() của java 8. Ở đây thì lấy 1 trường nào đó trong danh sách và ánh xạ sang mảng khác
        .map(FieldError::getDefaultMessage)        // ánh xạ
        .toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    categoryService.createCategory(categoryDTO);
    return ResponseEntity.ok("Inserted category successfully"+ categoryDTO);
  }
  // Phương thức GET:  Hiển thị tất cả Categories
//  @GetMapping("")
//  public ResponseEntity<String> getAllCategories () {
//    return ResponseEntity.ok("Đây là getAllCategories");
//  }

  // GET: kèm theo các parameter
  @GetMapping("")
  public ResponseEntity<List<Category>> getAllCategories (
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    List<Category> categories = categoryService.getAllCategory();
    return ResponseEntity.ok(categories);
  }

  // PUT
  @PutMapping("/{id}")
  public ResponseEntity<String> updateCategory (
    @PathVariable Long id,
    @Valid @RequestBody CategoryDTO categoryDTO
  ) {
    categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok("Updated category successfully");
  }

  // DELETE
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory (@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Deleted category successfully");
  }
}
