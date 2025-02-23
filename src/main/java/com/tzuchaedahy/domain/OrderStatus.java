package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.Objects;
import java.util.UUID;

public class OrderStatus {
    private UUID id;
    private String name;

    public OrderStatus() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty() || name.isBlank()) {
            throw new DomainException("order status name cannot be empty.");
        }

        this.name = name.trim().toLowerCase();
    }

    public static OrderStatus defaultStatus() {
        var orderStatus = new OrderStatus();
        orderStatus.setId(UUID.fromString("1e6ea845-8af0-4468-afb5-b9e6bed8738b"));
        orderStatus.setName("em espera");

        return orderStatus;
    }

    public static OrderStatus acceptedStatus() {
        var orderStatus = new OrderStatus();
        orderStatus.setId(UUID.fromString("75040965-0e33-4eba-a94a-4f21d3be3ce0"));
        orderStatus.setName("aceito");

        return orderStatus;
    }

    public static OrderStatus declinedStatus() {
        var orderStatus = new OrderStatus();
        orderStatus.setId(UUID.fromString("8ad87d55-cac7-4bc3-992b-7de5dc71704e"));
        orderStatus.setName("recusado");

        return orderStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderStatus that = (OrderStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }
}
