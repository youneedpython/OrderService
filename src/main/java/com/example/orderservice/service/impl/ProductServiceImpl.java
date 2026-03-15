package com.example.orderservice.service.impl;

import com.example.orderservice.domain.Product;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto.Response createProduct(ProductDto.CreateRequest request) {
        Product product = new Product(request.name(), request.price(), request.description());
        return toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto.Response getProduct(Long productId) {
        return toResponse(findProductById(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto.Response> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductDto.Response updateProduct(Long productId, ProductDto.UpdateRequest request) {
        Product product = findProductById(productId);
        product.updateInfo(request.name(), request.price(), request.description());
        return toResponse(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));
    }

    private ProductDto.Response toResponse(Product product) {
        return new ProductDto.Response(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription()
        );
    }
}
