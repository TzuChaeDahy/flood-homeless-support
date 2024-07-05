package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.services.ItemDonationService;

import java.util.List;

public class ItemDonationController {
    private final ItemDonationService itemDonationService;

    public ItemDonationController() {
        this.itemDonationService = new ItemDonationService();
    }

    public void save(ItemDonation itemDonation) {
        itemDonationService.save(itemDonation);
    }

    public List<ItemDonation> findAllSimplifiedByDistributionCenter(DistributionCenter distributionCenter) {
        return itemDonationService.findAllSimplifiedByDistributionCenter(distributionCenter);
    }
}
