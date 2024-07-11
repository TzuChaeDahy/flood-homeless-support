package com.tzuchaedahy.services;

import java.util.List;
import java.util.UUID;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.domain.OrderItem;
import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.repositories.OrderItemRepository;

public class OrderItemService {
    public OrderItemRepository orderItemRepository;

    public OrderItemService() {
        orderItemRepository = new OrderItemRepository();
    }

    public void saveAll(List<OrderItem> orderItems) {
        orderItemRepository.saveAll(orderItems);
    }

    public OrderItem findByOrderID(UUID orderID) {
        return orderItemRepository.findByOrderID(orderID);
    }

    public boolean canReceiveOrder(Shelter shelter, ItemType itemType, Integer amount) {
        int quantity = orderItemRepository.countItemsByDistributionCenterAndType(shelter, itemType);

        if (quantity + amount > 200) {
            return false;
        }

        return true;
    }
}
