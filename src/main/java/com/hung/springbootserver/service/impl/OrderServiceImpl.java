package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.OrderDao;
import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.BuyItem;
import com.hung.springbootserver.dto.OrderCreatingRequest;
import com.hung.springbootserver.dto.OrderQueryParams;
import com.hung.springbootserver.model.Order;
import com.hung.springbootserver.model.OrderItem;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.OrderService;
import com.hung.springbootserver.util.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Transactional
    @Override
    public Integer createOrder(Integer userId, OrderCreatingRequest orderCreatingRequest) {
        //取得商品資訊
        List<OrderItem> orderItemList =new ArrayList<>();
        Integer totalAmount = 0;

        for(BuyItem buyItem: orderCreatingRequest.getBuyItemList()){
            Product product= productDao.queryProductById(buyItem.getProductId());

            //檢查商品是否存在
            if(product == null){
                log.warn("商品ID:{} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //檢查商品庫存是否足夠
            else if(product.getPrice() < buyItem.getQuantity()){
                log.warn("商品ID:{} 庫存不足");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //扣除庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

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

    @Override
    public Order queryOrderById(Integer orderId) {
        Order order = orderDao.queryOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.queryOrderItemById(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public Page<Order> queryOrders(OrderQueryParams orderQueryParams) {

        List<Order> orderList = orderDao.queryOrders(orderQueryParams);

        for(Order order:orderList) {
            List<OrderItem> orderItemList = orderDao.queryOrderItemById(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        Integer count = orderDao.countOrder(orderQueryParams.getUserId());

        Page<Order> page = new Page<>();
        page.setLimit(orderQueryParams.getLimit());
        page.setOffset(orderQueryParams.getOffset());
        page.setTotal(count);
        page.setResult(orderList);

        return page;
    }
}
