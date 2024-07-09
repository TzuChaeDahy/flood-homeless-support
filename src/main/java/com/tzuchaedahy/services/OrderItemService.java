package com.tzuchaedahy.services;

import java.util.List;

import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.repositories.OrderItemRepository;

public class OrderItemService {
    public OrderItemRepository orderItemRepository;

    public OrderItemService() {
        orderItemRepository = new OrderItemRepository();
    }

    public void saveAll(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }
}
