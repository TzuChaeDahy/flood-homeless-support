package com.tzuchaedahy.controllers;

import java.util.List;
import java.util.UUID;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.services.OrderService;

public class OrderController {
    public OrderService orderService;

    public OrderController() {
        orderService = new OrderService();
    }

    public void save(Order order) {
        orderService.save(order);
    }

    public List<Order> pendingOrders(DistributionCenter distributionCenter) {
        return orderService.pendingOrders(distributionCenter);
    }

    public void changeStatus(UUID orderID, UUID orderStatusID, String description) {
        orderService.changeStatus(orderID, orderStatusID, description);
    }
}
