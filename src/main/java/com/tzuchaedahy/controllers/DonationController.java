package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.services.DonationService;

public class DonationController {
    private final DonationService donationService;

    public DonationController() {
        this.donationService = new DonationService();
    }

    public void save(Donation donation) {
        donationService.save(donation);
    }
}
