package com.tzuchaedahy.controllers;

import java.util.List;
import java.util.UUID;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.services.OrderItemService;

public class OrderItemController {
    public OrderItemService orderItemService;

    public OrderItemController() {
        orderItemService = new OrderItemService();
    }

    public void saveAll(List<OrderItem> orderItems) {
        orderItemService.saveAll(orderItems);
    }

    public OrderItem findByOrderID(UUID orderID) {
        return orderItemService.findByOrderID(orderID);
    }

    public boolean canReceiveOrder(Shelter shelter, ItemType itemType, Integer amount) {
        return orderItemService.canReceiveOrder(shelter, itemType, amount);
    }
}
