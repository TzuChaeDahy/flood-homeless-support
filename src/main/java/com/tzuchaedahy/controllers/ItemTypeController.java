package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.ItemType;
import com.tzuchaedahy.services.ItemTypeService;

import java.util.HashMap;
import java.util.Map;

public class ItemTypeController {
    private ItemTypeService itemTypeService;

    public ItemTypeController() {
        this.itemTypeService = new ItemTypeService();
    }

    public Map<String, ItemType> findAll() {
        var itemTypes = itemTypeService.findAll().toArray();

        Map<String, ItemType> itemTypeMap = new HashMap<>();
        for (Integer i = 1; i <= itemTypes.length; i++) {
            itemTypeMap.put(i.toString(), (ItemType) itemTypes[i - 1]);
        }

        return itemTypeMap;
    }
}
