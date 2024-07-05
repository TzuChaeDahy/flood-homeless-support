package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.DistributionCenter;
import com.tzuchaedahy.services.DistributionCenterService;

import java.util.HashMap;
import java.util.Map;

public class DistributionCenterController {
    private final DistributionCenterService distributionCenterService;

    public DistributionCenterController() {
        this.distributionCenterService = new DistributionCenterService();
    }

    public Map<String, DistributionCenter> findAll() {
        var distributionCenters = distributionCenterService.findAll().toArray();

        Map<String, DistributionCenter> distributionCenterMap = new HashMap<>();
        for (Integer i = 1; i <= distributionCenters.length; i++) {
            distributionCenterMap.put(i.toString(), (DistributionCenter) distributionCenters[i - 1]);
        }

        return distributionCenterMap;
    }

    public DistributionCenter findByName(String name) {
        return distributionCenterService.findByName(name);
    }
}
