package com.tdt.shop.services;

import com.tdt.shop.dtos.ProductDTO;
import com.tdt.shop.dtos.ProductImageDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.InvalidParamException;
import com.tdt.shop.exceptions.UniqueDataExistedException;
import com.tdt.shop.models.Product;
import com.tdt.shop.models.ProductImage;
import com.tdt.shop.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
  Product createProduct (ProductDTO productDTO) throws DataNotFoundException, UniqueDataExistedException;
  Product getProductById (long id) throws DataNotFoundException;
  Page<ProductResponse> getAllProducts (PageRequest pageRequest);
  Product updateProduct (long id, ProductDTO productDTO) throws DataNotFoundException;
  void deleteProduct (long id) throws DataNotFoundException;
  boolean existByName (String name) throws UniqueDataExistedException;
  ProductImage createProductImage (Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;
}
