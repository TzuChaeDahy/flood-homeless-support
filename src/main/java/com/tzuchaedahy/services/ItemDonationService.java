package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.ItemDonation;
import com.tzuchaedahy.repositories.ItemDonationRepository;

public class ItemDonationService {
    private final ItemDonationRepository itemDonationRepository;

    public ItemDonationService() {
        this.itemDonationRepository = new ItemDonationRepository();
    }

    public void save(ItemDonation itemDonation) {
        itemDonationRepository.save(itemDonation);
    }
}
