package com.tdt.shop.repositories;

import com.tdt.shop.models.Order;
import com.tdt.shop.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  // Tìm các đơn hàng của 1 user nào đó
  List<Order> findByUserId (Long userId);

  @Query("SELECT o, od, p FROM Order o "
    + "JOIN OrderDetail od ON o.id = od.order.id "
    + "JOIN Product p ON od.product.id = p.id "
    + "WHERE o.id = :order_id ")
  List<Object[]> getOrderAndOrderDetailListResponseByOrderId (@Param("order_id") Long orderId);
}
