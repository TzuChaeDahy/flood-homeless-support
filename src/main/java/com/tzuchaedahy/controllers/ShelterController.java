package com.tzuchaedahy.controllers;

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
}
