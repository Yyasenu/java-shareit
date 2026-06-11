package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long ownerId, ItemDto dto);

    ItemDto updateItem(Long ownerId, ItemDto dto);

    ItemDto getItemById(Long id);

    List<ItemDto> getItemsByOwnerId(Long ownerId);

    List<ItemDto> search(String text);
}