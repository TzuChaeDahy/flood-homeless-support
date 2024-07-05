package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Item {
    private UUID id;
    private String name;
    private final Map<String, String> attributes = new HashMap<>();

    private ItemType itemType;

    public Item() {
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
            throw new DomainException("item name cannot be empty.");
        }

        this.name = name.trim().toLowerCase();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes.putAll(attributes);
    }

    public String attributesToJson() {
        if (attributes.isEmpty()) {
            return "{}";
        }
        String json = "";


        for (String key : attributes.keySet()) {
            json += "\"" + key + "\":\"" + attributes.get(key) + "\"" + ",";
        }

        return "{" + json.substring(0, json.length() - 1) + "}";
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(name, item.name) && Objects.equals(attributes, item.attributes) && Objects.equals(itemType, item.itemType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(attributes);
        result = 31 * result + Objects.hashCode(itemType);
        return result;
    }
}
