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

    public ItemType findByName(String name) {
        return itemTypeRepository.findByName(name);
    }

    public boolean isItemNamePossible(ItemType itemType, String name) {
        var defaultNames = itemTypeRepository.findDefaultNames(itemType);

        for (String defaultName : defaultNames) {
            if (name.equals(defaultName)) {
                return true;
            }
        }

        return false;
    }
}
