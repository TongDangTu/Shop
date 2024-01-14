package com.tdt.shop.services;

import com.tdt.shop.dtos.CategoryDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.UniqueDataExistedException;
import com.tdt.shop.models.Category;

import java.util.List;

public interface ICategoryService {
  Category createCategory (CategoryDTO category) throws UniqueDataExistedException;
  Category getCategoryById (long id) throws DataNotFoundException;
  List<Category> getAllCategory ();
  Category updateCategory(long id, CategoryDTO category) throws DataNotFoundException, UniqueDataExistedException;
  void deleteCategory (long id) throws DataNotFoundException;
  boolean existByName (String name) throws UniqueDataExistedException;
}
