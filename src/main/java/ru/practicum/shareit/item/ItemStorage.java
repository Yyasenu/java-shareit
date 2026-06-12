package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {

    Optional<Item> getItemById(long id);

    Collection<Item> getAll(long userId);

    Item create(Item item);

    Item update(long id, long userId, Item itemUpdateDto);

    Collection<Item> search(String text);
}