package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.repositories.DistributionCenterRepository;

import java.util.List;

public class DistributionCenterService {
    private final DistributionCenterRepository distributionCenterRepository;

    public DistributionCenterService() {
        this.distributionCenterRepository = new DistributionCenterRepository();
    }

    public List<DistributionCenter> findAll() {
        return distributionCenterRepository.findAll();
    }
}
