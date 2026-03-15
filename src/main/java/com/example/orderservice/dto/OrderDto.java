package com.example.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderDto {

    public record CreateRequest(
            @NotBlank(message = "고객 이름은 필수입니다.")
            String customerName,

            @NotEmpty(message = "주문 항목은 1개 이상이어야 합니다.")
            List<OrderItemRequest> items
    ) {}

    public record OrderItemRequest(
            Long productId,

            @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
            int quantity
    ) {}

    public record Response(
            Long id,
            String customerName,
            String status,
            List<OrderItemResponse> items,
            long totalPrice,
            String createdAt
    ) {}

    public record OrderItemResponse(
            Long productId,
            String productName,
            int quantity,
            long unitPrice,
            long totalPrice
    ) {}
}
