package com.example.orderservice.service;

import com.example.orderservice.dto.ProductDto;
import java.util.List;

public interface ProductService {

    ProductDto.Response createProduct(ProductDto.CreateRequest request);

    ProductDto.Response getProduct(Long productId);

    List<ProductDto.Response> getAllProducts();

    ProductDto.Response updateProduct(Long productId, ProductDto.UpdateRequest request);

    void deleteProduct(Long productId);
}
