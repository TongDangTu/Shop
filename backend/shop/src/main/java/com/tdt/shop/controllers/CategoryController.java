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

  @GetMapping("")
  public ResponseEntity<?> getAllCategories (
    @RequestParam("page") int page,
    @RequestParam("limit") int limit
  ) {
    try {
      List<Category> categories = categoryService.getAllCategory();
      return ResponseEntity.ok(categories);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getCategory (
    @PathVariable int id
  ) {
    try {
      Category category = categoryService.getCategoryById(id);
      return ResponseEntity.ok(category);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

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
    try {
      Category category = categoryService.createCategory(categoryDTO);
      return ResponseEntity.ok(category);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }


  @PutMapping("/{id}")
  public ResponseEntity<?> updateCategory (
    @PathVariable Long id,
    @Valid @RequestBody CategoryDTO categoryDTO
  ) {
    try {
      Category category = categoryService.updateCategory(id, categoryDTO);
      return ResponseEntity.ok(category);
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteCategory (@PathVariable Long id) {
    try {
      categoryService.deleteCategory(id);
      return ResponseEntity.ok(new MessageResponse("Xóa thành công"));
    }
    catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }
}
