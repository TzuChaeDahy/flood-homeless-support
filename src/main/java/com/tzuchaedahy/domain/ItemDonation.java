package com.tzuchaedahy.domain;

import com.tzuchaedahy.exceptions.DomainException;

import java.util.Objects;
import java.util.UUID;

public class ItemDonation {
    private Integer quantity;

    private Item item;
    private Donation donation;

    public ItemDonation() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Donation getDonation() {
        return donation;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
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

        ItemDonation that = (ItemDonation) o;
        return Objects.equals(quantity, that.quantity) && Objects.equals(item, that.item) && Objects.equals(donation, that.donation);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(quantity);
        result = 31 * result + Objects.hashCode(item);
        result = 31 * result + Objects.hashCode(donation);
        return result;
    }
}
