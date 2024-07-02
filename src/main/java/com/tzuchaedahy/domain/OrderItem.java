package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.Objects;

public class OrderItem {
    private Integer quantity;

    private Order order;
    private Item item;

    public OrderItem() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity < 1) {
            throw new DomainException("quantity cannot be less than one.");
        }

        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(quantity, orderItem.quantity) && Objects.equals(order, orderItem.order) && Objects.equals(item, orderItem.item);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(quantity);
        result = 31 * result + Objects.hashCode(order);
        result = 31 * result + Objects.hashCode(item);
        return result;
    }
}
