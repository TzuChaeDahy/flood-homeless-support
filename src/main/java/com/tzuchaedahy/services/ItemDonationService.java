package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.repositories.ItemDonationRepository;

import java.util.List;

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
}
