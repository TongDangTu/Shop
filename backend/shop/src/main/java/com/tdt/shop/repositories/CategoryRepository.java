package com.tdt.shop.repositories;

import com.tdt.shop.models.Category;
import com.tdt.shop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// extends từ JpaRepository<> với generic 1 là kiểu dữ liệu thực thể, generic 2 là kiểu dữ liệu ID
//@Repository // do đã extends JpaRepository nên dòng này thừa, không cần thiết
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
