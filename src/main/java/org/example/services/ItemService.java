package org.example.services;

import org.example.models.Item;
import org.example.models.Person;
import org.example.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findByItemName(String itemName){
        return itemRepository.findByItemName(itemName);
    }

    public List<Item> findByOwner(Person owner){
        return itemRepository.findByOwner(owner);
    }
}
