package com.tdt.shop.services;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.UniqueDataExistedException;
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
  public Category getCategoryById(long id) throws DataNotFoundException {
    return categoryRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Danh mục không tồn tại"));
  }

  @Override
  public List<Category> getAllCategory() {
    return categoryRepository.findAll();
  }

  @Override
  public Category createCategory(CategoryDTO categoryDTO) throws UniqueDataExistedException {
    existByName(categoryDTO.getName());
    Category category = Category.builder()
      .name(categoryDTO.getName())
      .build();
    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategory(long id, CategoryDTO categoryDTO) throws DataNotFoundException, UniqueDataExistedException {
    Category existingCategory = getCategoryById(id);
    existByName(categoryDTO.getName());
    existingCategory.setName(categoryDTO.getName());
    return categoryRepository.save(existingCategory);
  }

  @Override
  public void deleteCategory(long id) throws DataNotFoundException {
    // Xóa cứng
    getCategoryById(id);
    categoryRepository.deleteById(id);
  }

  @Override
  public boolean existByName (String name) throws UniqueDataExistedException {
    if (categoryRepository.existsByName(name)) {
      throw new UniqueDataExistedException("Tên danh mục đã tồn tại");
    }
    return true;
  }
}
