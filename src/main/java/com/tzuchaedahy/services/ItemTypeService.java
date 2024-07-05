package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.repositories.ItemTypeRepository;

import java.util.List;

public class ItemTypeService {
    private ItemTypeRepository itemTypeRepository;

    public ItemTypeService() {
        this.itemTypeRepository = new ItemTypeRepository();
    }

    public List<ItemType> findAll() {
        return this.itemTypeRepository.findAll();
    }
}
