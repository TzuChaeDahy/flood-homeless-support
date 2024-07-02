package com.tzuchaedahy.domain;

import java.util.Objects;
import java.util.UUID;

public class Donation {
    private UUID id;

    private DistributionCenter distributionCenter;

    public Donation() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DistributionCenter getDistributionCenter() {
        return distributionCenter;
    }

    public void setDistributionCenter(DistributionCenter distributionCenter) {
        this.distributionCenter = distributionCenter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Donation donation = (Donation) o;
        return Objects.equals(id, donation.id) && Objects.equals(distributionCenter, donation.distributionCenter);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(distributionCenter);
        return result;
    }
}
