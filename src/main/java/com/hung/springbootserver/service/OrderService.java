package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.CreateOrderRequest;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
