package com.example.orderservice.service.impl;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.Product;
import com.example.orderservice.domain.Stock;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.exception.OutOfStockException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private StockRepository stockRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        stockRepository = mock(StockRepository.class);
        orderService = new OrderServiceImpl(orderRepository, productRepository, stockRepository);
    }

    @Test
    void placeOrder_success_whenStockIsOne() {
        // given
        Product product = new Product("테스트상품", 1000, "설명");
        Stock stock = new Stock(product, 1);

        OrderDto.CreateRequest request = new OrderDto.CreateRequest(
                "고객A",
                List.of(new OrderDto.OrderItemRequest(1L, 1))
        );

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdWithLock(anyLong())).thenReturn(Optional.of(stock));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        OrderDto.Response response = orderService.createOrder(request);

        // then
        assertEquals("고객A", response.customerName());
        assertEquals(1, response.items().size());
        assertEquals(0, stock.getQuantity()); // 재고 차감 확인
    }

    @Test
    void placeOrder_fail_whenStockIsZero() {
        // given
        Product product = new Product("테스트상품", 1000, "설명");
        Stock stock = new Stock(product, 0);

        OrderDto.CreateRequest request = new OrderDto.CreateRequest(
                "고객A",
                List.of(new OrderDto.OrderItemRequest(1L, 1))
        );

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(stockRepository.findByProductIdWithLock(anyLong())).thenReturn(Optional.of(stock));

        // when & then
        assertThrows(OutOfStockException.class, () -> orderService.createOrder(request));
        assertEquals(0, stock.getQuantity()); // 재고 변화 없음
    }
}
