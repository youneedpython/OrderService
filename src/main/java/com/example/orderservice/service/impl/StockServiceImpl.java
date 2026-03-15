package com.example.orderservice.service.impl;

import com.example.orderservice.domain.Product;
import com.example.orderservice.domain.Stock;
import com.example.orderservice.dto.StockDto;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.StockRepository;
import com.example.orderservice.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public StockServiceImpl(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StockDto.Response getStock(Long productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stock for Product", productId));
        return toResponse(stock);
    }

    @Override
    public StockDto.Response increaseStock(Long productId, StockDto.AdjustRequest request) {
        Stock stock = stockRepository.findByProductIdWithLock(productId)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));
                    return stockRepository.save(new Stock(product, 0));
                });

        stock.increase(request.quantity());
        return toResponse(stock);
    }

    @Override
    public StockDto.Response decreaseStock(Long productId, StockDto.AdjustRequest request) {
        Stock stock = stockRepository.findByProductIdWithLock(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stock for Product", productId));
        stock.decrease(request.quantity());
        return toResponse(stock);
    }

    private StockDto.Response toResponse(Stock stock) {
        return new StockDto.Response(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getQuantity()
        );
    }
}
