package com.tdt.shop.services;

import com.tdt.shop.dtos.ProductImageDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.Product;
import com.tdt.shop.models.ProductImage;
import com.tdt.shop.repositories.ProductImageRepository;
import com.tdt.shop.repositories.ProductRepository;
import com.tdt.shop.responses.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {
  private final ProductRepository productRepository;
  private final ProductImageRepository productImageRepository;

  @Override
  public ProductImage getProductImage(Long id) throws DataNotFoundException {
    return productImageRepository.findById(id)
      .orElseThrow(() -> new DataNotFoundException("Ảnh sản phẩm không tồn tại"));
  }

  @Override
  public ProductImage createProductImage(ProductImageDTO productImageDTO) {
    return null;
  }

  @Override
  public ProductImage updateProductImage(Long id, ProductImageDTO productImageDTO) {
    return null;
  }

  @Override
  public void deleteProductImage(Long id) {

  }

  @Override
  public List<ProductImage> getProductImageByProductId(Long productId) throws DataNotFoundException {
    Product productExisting = productRepository.findById(productId)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm có id = "+ productId));
    return productImageRepository.findByProductId(productId);
  }
}
