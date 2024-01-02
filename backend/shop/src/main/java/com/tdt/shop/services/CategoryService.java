package com.tdt.shop.services;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.models.Category;
import com.tdt.shop.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor  // Tự động tạo constructor cho các thuộc tính final tương ứng
public class CategoryService implements ICategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(CategoryDTO categoryDTO) {
    Category newCategory = Category.builder()
      .name(categoryDTO.getName())
      .build();
    return categoryRepository.save(newCategory);
  }

  @Override
  public Category getCategoryById(long id) {
    return categoryRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
  }

  @Override
  public List<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  @Override
  public Category updateCategory(long id, CategoryDTO categoryDTO) {
    Category existingCategory = getCategoryById(id);
    existingCategory.setName(categoryDTO.getName());
    categoryRepository.save(existingCategory);
    return existingCategory;
  }

  @Override
  public void deleteCategory(long id) {
    // Xóa cứng
    categoryRepository.deleteById(id);
  }
}
