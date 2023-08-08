package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.OrderCreatingRequest;
import com.hung.springbootserver.dto.OrderQueryParams;
import com.hung.springbootserver.model.Order;
import com.hung.springbootserver.util.Page;

public interface OrderService {

    Integer createOrder(Integer userId, OrderCreatingRequest orderCreatingRequest);

    Order queryOrderById(Integer orderId);

    Page<Order> queryOrders(OrderQueryParams orderQueryParams);
}
