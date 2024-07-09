package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.repositories.ItemDonationRepository;

import java.util.List;
import java.util.Map;

public class ItemDonationService {
    private final ItemDonationRepository itemDonationRepository;

    public ItemDonationService() {
        this.itemDonationRepository = new ItemDonationRepository();
    }

    public void save(ItemDonation itemDonation) {
        itemDonationRepository.save(itemDonation);
    }

    public List<ItemDonation> findAllSimplifiedByDistributionCenter(DistributionCenter distributionCenter) {
        return itemDonationRepository.findAllSimplifiedByDistributionCenter(distributionCenter);
    }

    public boolean canReceiveDonation(DistributionCenter distributionCenter, ItemType itemType, Integer amount) {
        int quantity = itemDonationRepository.countItemsByDistributionCenterAndType(distributionCenter, itemType);

        if (quantity + amount > 1000) {
            return false;
        }

        return true;
    }

    public Map<String, List<ItemDonation>> findAvailableItemsByName(String name) {
        return itemDonationRepository.findAvailableItemsByName(name);
    }
}
