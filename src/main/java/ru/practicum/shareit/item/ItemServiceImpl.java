package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserService userService;

    private final java.util.Map<Long, Item> items = new java.util.concurrent.ConcurrentHashMap<>();
    private Long nextItemId = 1L;

    @Override
    public ItemDto addItem(Long ownerId, ItemDto dto) {
        User owner = userService.getUserById(ownerId);
        if (owner == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с идентификатором " + ownerId + " не найден");
        }

        Item item = itemMapper.toModel(dto);
        item.setId(nextItemId++);
        item.setOwner(owner);

        if (item.getAvailable() == null) {
            item.setAvailable(false);
        }

        items.put(item.getId(), item);
        return toDtoWithStats(item);
    }

    @Override
    public ItemDto updateItem(Long ownerId, ItemDto dto) {
        Long itemId = dto.getId();
        Item existing = items.get(itemId);

        if (existing == null) {
            throw new IllegalArgumentException("Товар не найден"); // Или ResponseStatusException 404
        }

        if (!existing.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Только владелец может обновить этот элемент");
        }

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        if (dto.getAvailable() != null) {
            existing.setAvailable(dto.getAvailable());
        }

        return toDtoWithStats(existing);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Вещь с id " + id + " не найдена");
        }
        return toDtoWithStats(item);
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(i -> i.getOwner().getId().equals(ownerId))
                .map(this::toDtoWithStats)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String lowerText = text.toLowerCase();
        return items.values().stream()
                .filter(i -> i.getAvailable() != null && i.getAvailable())
                .filter(i ->
                        i.getName().toLowerCase().contains(lowerText) ||
                                (i.getDescription() != null && i.getDescription().toLowerCase().contains(lowerText))
                )
                .map(this::toDtoWithStats)
                .collect(Collectors.toList());
    }

    private ItemDto toDtoWithStats(Item item) {
        ItemDto dto = itemMapper.toDto(item);
        dto.setRentalCount(0);
        return dto;
    }
}