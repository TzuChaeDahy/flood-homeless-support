package com.tzuchaedahy.controllers;

import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.services.ItemService;

public class ItemController {
    private final ItemService itemService;

    public ItemController() {
        this.itemService = new ItemService();
    }

    public void save(Item item) {
        itemService.save(item);
    }
}
