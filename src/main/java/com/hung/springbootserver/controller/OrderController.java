package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.OrderCreatingRequest;
import com.hung.springbootserver.dto.OrderQueryParams;
import com.hung.springbootserver.model.Order;
import com.hung.springbootserver.service.OrderService;
import com.hung.springbootserver.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/users/{userId}/order")
    public ResponseEntity<Page<Order>> queryOrders(@PathVariable Integer userId,
                                            //分頁
                                            @RequestParam(defaultValue = "5") @Min(5) @Max(20) Integer limit,
                                            @RequestParam(defaultValue = "0") @Min(0) Integer offset){
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        Page<Order> orderPage = orderService.queryOrders(orderQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(orderPage);
    }

    @PostMapping("users/{userId}/order")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                             @RequestBody @Valid OrderCreatingRequest orderCreatingRequest){

        Integer orderId = orderService.createOrder(userId, orderCreatingRequest);

        Order order = orderService.queryOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
