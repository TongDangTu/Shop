package com.tdt.shop.services;

import com.tdt.shop.dtos.ProductDTO;
import com.tdt.shop.dtos.ProductImageDTO;
import com.tdt.shop.exceptions.DataNotFoundException;
import com.tdt.shop.exceptions.InvalidParamException;
import com.tdt.shop.exceptions.UniqueDataExistedException;
import com.tdt.shop.models.Category;
import com.tdt.shop.models.Product;
import com.tdt.shop.models.ProductImage;
import com.tdt.shop.repositories.CategoryRepository;
import com.tdt.shop.repositories.ProductImageRepository;
import com.tdt.shop.repositories.ProductRepository;
import com.tdt.shop.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductImageRepository productImageRepository;

  @Override
  public Product getProductById(long productId) throws DataNotFoundException {
    return productRepository.findById(productId)
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm"));
  }

  @Override
  public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
    return productRepository.findAll(pageRequest).map(product -> ProductResponse.fromProduct(product));
  }

  @Override
  public Product createProduct(ProductDTO productDTO) throws DataNotFoundException, UniqueDataExistedException {
    existByName(productDTO.getName());
    Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục có id: "+ productDTO.getCategoryId()));
    Product newProduct = Product.builder()
      .name(productDTO.getName())
      .price(productDTO.getPrice())
      .thumbnail(productDTO.getThumbnail())
      .description(productDTO.getDescription())
      .category(existingCategory)
      .build();
    return productRepository.save(newProduct);
  }

  @Override
  public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
    Product existingProduct = getProductById(id);
    Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
      .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục có id: "+ productDTO.getCategoryId()));
    existingProduct.setName(productDTO.getName());
    existingProduct.setCategory(existingCategory);
    existingProduct.setPrice(productDTO.getPrice());
    existingProduct.setThumbnail(productDTO.getThumbnail());
    existingProduct.setDescription(productDTO.getDescription());
    existingProduct.setThumbnail(productDTO.getThumbnail());
    return productRepository.save(existingProduct);
  }

  @Override
  public void deleteProduct(long id) throws DataNotFoundException {
    getProductById(id);
    productRepository.deleteById(id);
  }

  @Override
  public boolean existByName(String name) throws UniqueDataExistedException {
    if (productRepository.existsByName(name)) {
      throw new UniqueDataExistedException("Tên sản phẩm đã tồn tại");
    }
    return true;
  }

  @Override
  public ProductImage createProductImage (Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
    Product existingProduct = productRepository.findById(productId)
      .orElseThrow(() -> new DataNotFoundException("Không tim thấy sản phẩm có id: "+ productImageDTO.getProductId()));
    ProductImage newProductImage = ProductImage.builder()
      .product(existingProduct)
      .imageUrl(productImageDTO.getImageUrl())
      .build();
    // Không cho insert quá 5 ảnh cho 1 sản phẩm
    int size = productImageRepository.findByProductId(productId).size();
    if (size >= ProductImage.MAXIMUM_IMAGES) {
      throw new InvalidParamException("Sản phẩm chỉ được phép có tối đa "+ ProductImage.MAXIMUM_IMAGES +" ảnh");
    }
    return productImageRepository.save(newProductImage);
  }
}
