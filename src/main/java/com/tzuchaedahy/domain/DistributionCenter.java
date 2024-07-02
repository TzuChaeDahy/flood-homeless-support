package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.Objects;
import java.util.UUID;

public class DistributionCenter {
    private UUID id;
    private String name;
    private String address;

    public DistributionCenter() {
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
            throw new DomainException("distribution center name cannot be empty.");
        }

        this.name = name.trim().toLowerCase();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address.isEmpty() || address.isBlank()) {
            throw new DomainException("distribution center address cannot be empty.");
        }
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistributionCenter that = (DistributionCenter) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(address);
        return result;
    }
}
