package com.tdt.shop.repositories;

import com.tdt.shop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
  // find...
  List<OrderDetail> findByOrderId (Long orderId);


}
