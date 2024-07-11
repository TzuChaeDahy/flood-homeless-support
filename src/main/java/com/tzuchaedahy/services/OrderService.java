package com.tzuchaedahy.services;

import java.util.List;
import java.util.UUID;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.Order;
import com.tzuchaedahy.repositories.OrderRepository;

public class OrderService {
    public OrderRepository orderRepository;

    public OrderService() {
        orderRepository = new OrderRepository();
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public List<Order> pendingOrders(DistributionCenter distributionCenter) {
        return orderRepository.pendingOrders(distributionCenter);
    }

        public void changeStatus(UUID orderID, UUID orderStatusID, String description) {
        orderRepository.changeStatus(orderID, orderStatusID, description);
    }
}
