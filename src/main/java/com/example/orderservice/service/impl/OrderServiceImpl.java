package com.example.orderservice.service.impl;

import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderItem;
import com.example.orderservice.domain.Product;
import com.example.orderservice.domain.Stock;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.StockRepository;
import com.example.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            StockRepository stockRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public OrderDto.Response createOrder(OrderDto.CreateRequest request) {
        Order order = new Order(request.customerName());

        for (OrderDto.OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Product", itemRequest.productId()));

            // 비관적 락으로 재고 조회
            Stock stock = stockRepository.findByProductIdWithLock(itemRequest.productId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Stock for Product", itemRequest.productId()));

            // 재고 부족 시 예외
            if (stock.getQuantity() < itemRequest.quantity()) {
                throw new com.example.orderservice.exception.OutOfStockException(
                        product.getId(), itemRequest.quantity(), stock.getQuantity());
            }

            // 재고 차감
            stock.decrease(itemRequest.quantity());

            order.addOrderItem(new OrderItem(product, itemRequest.quantity()));
        }

        // 주문 저장
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto.Response getOrder(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));
        return toResponse(order);
    }

    @Override
    public OrderDto.Response confirmOrder(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));
        order.confirm();
        return toResponse(order);
    }

    @Override
    public OrderDto.Response cancelOrder(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> ResourceNotFoundException.of("Order", orderId));
        order.cancel();

        // 취소 시 재고 복원
        for (OrderItem item : order.getOrderItems()) {
            stockRepository.findByProductIdWithLock(item.getProduct().getId())
                    .ifPresent(stock -> stock.increase(item.getQuantity()));
        }

        return toResponse(order);
    }

    private OrderDto.Response toResponse(Order order) {
        List<OrderDto.OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderDto.OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                ))
                .toList();

        long totalPrice = items.stream().mapToLong(OrderDto.OrderItemResponse::totalPrice).sum();

        return new OrderDto.Response(
                order.getId(),
                order.getCustomerName(),
                order.getStatus().name(),
                items,
                totalPrice,
                order.getCreatedAt().toString()
        );
    }
}
