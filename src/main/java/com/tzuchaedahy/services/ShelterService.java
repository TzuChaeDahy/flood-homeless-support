package com.tzuchaedahy.services;

import java.util.List;

import com.tzuchaedahy.domain.Shelter;
import com.tzuchaedahy.repositories.ShelterRepository;

public class ShelterService {
    private ShelterRepository shelterRepository;

    public ShelterService() {
        this.shelterRepository = new ShelterRepository();
    }

    public void save(Shelter shelter) {
        shelterRepository.save(shelter);
    }

    public List<Shelter> findAll() {
        return shelterRepository.findAll();
    }
}
