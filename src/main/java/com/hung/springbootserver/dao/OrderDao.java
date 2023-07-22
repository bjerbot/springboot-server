package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.BuyItem;
import com.hung.springbootserver.dto.CreateOrderRequest;
import com.hung.springbootserver.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItem(Integer orderId, List<OrderItem> orderItemList);
}
