package com.tzuchaedahy.services;

import com.tzuchaedahy.domain.Item;
import com.tzuchaedahy.repositories.ItemRepository;

public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService() {
        this.itemRepository = new ItemRepository();
    }

    public void save(Item item) {
        itemRepository.save(item);
    }
}
