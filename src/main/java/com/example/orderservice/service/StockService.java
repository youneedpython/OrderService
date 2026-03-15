package com.example.orderservice.service;

import com.example.orderservice.dto.StockDto;

public interface StockService {

    StockDto.Response getStock(Long productId);

    StockDto.Response increaseStock(Long productId, StockDto.AdjustRequest request);

    StockDto.Response decreaseStock(Long productId, StockDto.AdjustRequest request);
}
