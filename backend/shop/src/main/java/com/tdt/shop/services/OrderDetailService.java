package com.tdt.shop.services;

import com.tdt.shop.dtos.OrderDetailDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Order;
import com.tdt.shop.models.OrderDetail;
import com.tdt.shop.models.Product;
import com.tdt.shop.repositories.OrderDetailRepository;
import com.tdt.shop.repositories.OrderRepository;
import com.tdt.shop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService {
  private final OrderDetailRepository orderDetailRepository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;

  @Override
  public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(orderId)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Đơn hàng có id: "+ orderId));
    return orderDetailRepository.findByOrderId(orderId);
  }

  @Override
  public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
    return orderDetailRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy chi tiết đơn hàng có id: "+ id));
  }

  @Override
  @Transactional
  public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
    Order order = orderRepository.findById(orderDetailDTO.getOrderId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng có id: "+ orderDetailDTO.getOrderId()));
    Product product = productRepository.findById(orderDetailDTO.getProductId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id: "+ orderDetailDTO.getProductId()));
    OrderDetail orderDetail = OrderDetail.builder()
      .order(order)
      .product(product)
      .price(orderDetailDTO.getPrice())
      .numberOfProducts(orderDetailDTO.getNumberOfProducts())
      .totalMoney(orderDetailDTO.getTotalMoney())
      .build();
    return orderDetailRepository.save(orderDetail);
  }

  @Override
  @Transactional
  public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Đơn hàng có id: "+ orderDetailDTO.getOrderId()));
    Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Sản phẩm có id: "+ orderDetailDTO.getProductId()));
    OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy Chi tiết đơn hàng id: "+ id));
    existingOrderDetail.setOrder(existingOrder);
    existingOrderDetail.setProduct(existingProduct);
    existingOrderDetail.setPrice(orderDetailDTO.getPrice());
    existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
    existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
    return orderDetailRepository.save(existingOrderDetail);
  }

  @Override
  @Transactional
  public void deleteOrderDetail(Long id) throws DataNotFoundException {
    getOrderDetail(id);
    orderDetailRepository.deleteById(id);
  }

  @Override
  @Transactional
  public List<OrderDetail> createOrderDetails (List<OrderDetailDTO> orderDetailDTOs) throws DataNotFoundException {
    List<OrderDetail> orderDetails = new ArrayList<>();
    for(OrderDetailDTO orderDetailDTO: orderDetailDTOs) {
      Order order = orderRepository.findById(orderDetailDTO.getOrderId())
        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy đơn hàng có id: "+ orderDetailDTO.getOrderId()));
      Product product = productRepository.findById(orderDetailDTO.getProductId())
        .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id: "+ orderDetailDTO.getProductId()));
      OrderDetail orderDetail = OrderDetail.builder()
        .order(order)
        .product(product)
        .price(orderDetailDTO.getPrice())
        .numberOfProducts(orderDetailDTO.getNumberOfProducts())
        .totalMoney(orderDetailDTO.getTotalMoney())
        .build();
      orderDetails.add(orderDetail);
    }
    return orderDetailRepository.saveAll(orderDetails);
  }
}
