package com.tdt.shop.repositories;

import com.tdt.shop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  // exits...
  boolean existsByName (String name);

  // Phân trang
  Page<Product> findAll (Pageable pageable);
}
