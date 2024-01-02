package com.tdt.shop.services;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.models.Category;

import java.util.List;

public interface ICategoryService {
  Category createCategory (CategoryDTO category);
  Category getCategoryById (long id);
  List<Category> getAllCategory ();
  Category updateCategory(long id, CategoryDTO category);
  void deleteCategory (long id);
}
