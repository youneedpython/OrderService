package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;

public interface OrderService {

    OrderDto.Response createOrder(OrderDto.CreateRequest request);

    OrderDto.Response getOrder(Long orderId);

    OrderDto.Response confirmOrder(Long orderId);

    OrderDto.Response cancelOrder(Long orderId);
}
