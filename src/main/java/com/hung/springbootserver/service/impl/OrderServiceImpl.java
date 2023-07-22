package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.OrderDao;
import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.BuyItem;
import com.hung.springbootserver.dto.CreateOrderRequest;
import com.hung.springbootserver.model.OrderItem;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //取得商品資訊
        List<OrderItem> orderItemList =new ArrayList<>();
        Integer totalAmount = 0;

        for(BuyItem buyItem:createOrderRequest.getBuyItemList()){
            Product product= productDao.queryProductById(buyItem.getProductId());

            //計算價格
            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;

            //轉換 BuyItem 成 OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);
        //創建訂單項目
        orderDao.createOrderItem(orderId, orderItemList);
        return orderId;
    }
}
