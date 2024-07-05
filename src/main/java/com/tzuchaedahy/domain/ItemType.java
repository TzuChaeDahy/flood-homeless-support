package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemType {
    private UUID id;
    private String name;
    private final List<String> defaultAttributes = new ArrayList<>();
    private final List<String> defaultNames = new ArrayList<>();

    public ItemType() {
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
            throw new DomainException("item type name cannot be empty.");
        }

        this.name = name.trim().toLowerCase();
    }

    public List<String> getDefaultAttributes() {
        return defaultAttributes;
    }

    public void setDefaultAttribute(String attribute) {
        this.defaultAttributes.add(attribute);
    }

    public List<String> getDefaultNames() {
        return defaultNames;
    }

    public void setDefaultName(String name) {
        this.defaultNames.add(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemType itemType = (ItemType) o;
        return Objects.equals(id, itemType.id) && Objects.equals(name, itemType.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }
}


