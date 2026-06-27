package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> getAll(long userId);

    ItemDto getItemById(long id, long userId);

    ItemDto add(ItemDto itemDto, long userId);

    ItemUpdateDto update(long id, long userId, ItemUpdateDto itemUpdateDto);

    Collection<ItemDto> search(String text, long userId);

    CommentDto addComment(long itemId, long userId, String text);
}