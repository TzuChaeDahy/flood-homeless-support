package com.tzuchaedahy.services;

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
}
