package com.tdt.shop.controllers;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.models.Category;
import com.tdt.shop.responses.MessageResponse;
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

public class CategoryController {
  private final CategoryService categoryService;

  @PostMapping("")
  public ResponseEntity<?> createCategory (
    @RequestBody @Valid CategoryDTO categoryDTO,
    BindingResult result
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()   // lấy danh sách lỗi
        .stream()              // .stream() của java 8. Ở đây thì lấy 1 trường nào đó trong danh sách và ánh xạ sang mảng khác
        .map(FieldError::getDefaultMessage)        // ánh xạ
        .toList();
      return ResponseEntity.badRequest().body(new MessageResponse(errorMessages.toString()));
    }
    categoryService.createCategory(categoryDTO);
    return ResponseEntity.ok(categoryDTO);
  }

  @GetMapping("")
  public ResponseEntity<List<Category>> getAllCategories (
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    List<Category> categories = categoryService.getAllCategory();
    return ResponseEntity.ok(categories);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateCategory (
    @PathVariable Long id,
    @Valid @RequestBody CategoryDTO categoryDTO
  ) {
    categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok(new MessageResponse("Cập nhật Category thành công"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory (@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok(new MessageResponse("Xóa Category thành công"));
  }
}
