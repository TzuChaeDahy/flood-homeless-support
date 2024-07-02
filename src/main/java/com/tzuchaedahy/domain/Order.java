package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.Objects;
import java.util.UUID;

public class Order {
    private UUID id;
    private String description;

    private OrderStatus status;
    private DistributionCenter distributionCenter;
    private Shelter shelter;

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.isEmpty() || description.isBlank()) {
            throw new DomainException("order description cannot be empty.");
        }

        this.description = description.trim().toLowerCase();
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DistributionCenter getDistributionCenter() {
        return distributionCenter;
    }

    public void setDistributionCenter(DistributionCenter distributionCenter) {
        this.distributionCenter = distributionCenter;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(description, order.description) && Objects.equals(status, order.status) && Objects.equals(distributionCenter, order.distributionCenter) && Objects.equals(shelter, order.shelter);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Objects.hashCode(distributionCenter);
        result = 31 * result + Objects.hashCode(shelter);
        return result;
    }
}
