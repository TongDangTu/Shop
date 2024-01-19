package com.tdt.shop.repositories;

import com.tdt.shop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
  // exits...
  boolean existsByName (String name);

  // Phân trang
  Page<Product> findAll (Pageable pageable);

  @Query("SELECT p FROM Product p "
    + "WHERE "
    + "(:search = '' OR LOWER(p.name) LIKE %:search% OR LOWER(p.description) LIKE %:search%) "
    + "AND "
    + "(:category_id = 0 OR p.category.id = :category_id)")
  Page<Product> search(@Param("search") String search, @Param("category_id") Long categoryId, Pageable pageable);
}
