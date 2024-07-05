package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.services.ItemDonationService;

public class ItemDonationController {
    private final ItemDonationService itemDonationService;

    public ItemDonationController() {
        this.itemDonationService = new ItemDonationService();
    }

    public void save(ItemDonation itemDonation) {
        itemDonationService.save(itemDonation);
    }
}
