package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.Donation;
import com.tzuchaedahy.repositories.DonationRepository;

public class DonationService {
    private final DonationRepository donationRepository;

    public DonationService() {
        this.donationRepository = new DonationRepository();
    }

    public void save(Donation donation) {
        donationRepository.save(donation);
    }
}
