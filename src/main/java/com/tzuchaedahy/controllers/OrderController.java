package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.services.OrderService;

public class OrderController {
    public OrderService orderService;

    public OrderController() {
        orderService = new OrderService();
    }

    public void save(Order order) {
        orderService.save(order);
    }
}
