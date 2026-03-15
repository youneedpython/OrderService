package com.example.orderservice.controller;

import com.example.orderservice.dto.StockDto;
import com.example.orderservice.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<StockDto.Response> getStock(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getStock(productId));
    }

    @PatchMapping("/{productId}/increase")
    public ResponseEntity<StockDto.Response> increaseStock(
            @PathVariable Long productId,
            @RequestBody @Valid StockDto.AdjustRequest request) {
        return ResponseEntity.ok(stockService.increaseStock(productId, request));
    }

    @PatchMapping("/{productId}/decrease")
    public ResponseEntity<StockDto.Response> decreaseStock(
            @PathVariable Long productId,
            @RequestBody @Valid StockDto.AdjustRequest request) {
        return ResponseEntity.ok(stockService.decreaseStock(productId, request));
    }
}
