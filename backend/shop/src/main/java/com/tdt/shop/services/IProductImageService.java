package com.tdt.shop.services;

import com.tdt.shop.dtos.ProductImageDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.models.ProductImage;
import com.tdt.shop.responses.ProductImageResponse;

import java.util.List;

public interface IProductImageService {
  ProductImage getProductImage (Long id) throws DataNotFoundException;
  ProductImage createProductImage (ProductImageDTO productImageDTO);
  ProductImage updateProductImage (Long id, ProductImageDTO productImageDTO);
  void deleteProductImage (Long id);
  List<ProductImage> getProductImageByProductId (Long productId) throws DataNotFoundException;
}
