package com.tzuchaedahy.controllers;

import java.util.List;
import java.util.Map;

import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.services.ShelterService;

public class ShelterController {
    private ShelterService shelterService;

    public ShelterController() {
        this.shelterService = new ShelterService();
    }

    public void save(Shelter shelter) {
        shelterService.save(shelter);
    }

    public List<Shelter> findAll() {
        return shelterService.findAll();
    }

    public Map<String, Integer> findDonatedItemTypesQuantities(Shelter shelter) {
        return shelterService.findDonatedItemTypesQuantities(shelter);
    }
}
