package com.example.orderservice.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(Long productId, int requested, int available) {
        super("상품 ID(" + productId + ")의 재고 부족: 요청 " + requested + ", 현재 " + available);
    }
}
