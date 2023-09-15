package org.example.repositories;

import org.example.models.Item;
import org.example.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByItemName(String itemName);

    List<Item> findByOwner(Person owner);
}
