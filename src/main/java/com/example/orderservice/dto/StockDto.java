package com.example.orderservice.dto;

import jakarta.validation.constraints.Min;

public class StockDto {

    public record AdjustRequest(
            @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
            int quantity
    ) {}

    public record Response(
            Long id,
            Long productId,
            String productName,
            int quantity
    ) {}
}
