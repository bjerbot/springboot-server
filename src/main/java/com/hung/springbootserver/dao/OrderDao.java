package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.OrderQueryParams;
import com.hung.springbootserver.model.Order;
import com.hung.springbootserver.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItem(Integer orderId, List<OrderItem> orderItemList);

    Order queryOrderById(Integer orderId);

    List<OrderItem> queryOrderItemById(Integer orderId);

    List<Order> queryOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(Integer UserId);
}
