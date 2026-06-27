package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Collection<ItemDto> getAll(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Item> items = itemRepository.findByOwnerId(userId);

        Map<Long, List<Comment>> commentsByItemId = new HashMap<>();
        if (!items.isEmpty()) {
            List<Long> itemIds = items.stream().map(Item::getId).toList();
            List<Comment> allComments = commentRepository.findByItemIdIn(itemIds);
            allComments.forEach(c ->
                    commentsByItemId.computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>()).add(c)
            );
        }

        return items.stream()
                .map(item -> toItemDtoWithExtra(item, commentsByItemId.get(item.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long id, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        List<Comment> comments = commentRepository.findByItemIdOrderByCreatedDesc(item.getId());
        return toItemDtoWithExtra(item, comments);
    }

    @Override
    public ItemDto add(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = toItem(itemDto);
        item.setOwner(owner);
        Item saved = itemRepository.save(item);
        return toItemDto(saved);
    }

    @Override
    public ItemUpdateDto update(long id, long userId, ItemUpdateDto patch) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (patch.getName() != null) item.setName(patch.getName());
        if (patch.getDescription() != null) item.setDescription(patch.getDescription());
        if (patch.getAvailable() != null) item.setAvailable(patch.getAvailable());

        Item updated = itemRepository.save(item);
        return toItemUpdateDto(updated);
    }

    @Override
    public Collection<ItemDto> search(String text, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Collection<Item> results = itemRepository.searchByText(text);
        results = results.stream()
                .filter(i -> i.getOwner() != null && i.getOwner().getId().equals(userId))
                .toList();

        Map<Long, List<Comment>> commentsByItemId = new HashMap<>();
        if (!results.isEmpty()) {
            List<Long> itemIds = results.stream().map(Item::getId).toList();
            List<Comment> allComments = commentRepository.findByItemIdIn(itemIds);
            allComments.forEach(c ->
                    commentsByItemId.computeIfAbsent(c.getItem().getId(), k -> new ArrayList<>()).add(c)
            );
        }

        return results.stream()
                .map(item -> toItemDtoWithExtra(item, commentsByItemId.get(item.getId())))
                .collect(Collectors.toList());
    }

    public CommentDto addComment(long itemId, long userId, String text) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        boolean hasRental = bookingRepository.existsByItemIdAndBookerIdAndStatusIn(
                itemId,
                userId,
                Arrays.asList(BookingStatus.APPROVED)
        );
        if (!hasRental) {
            throw new ValidationException("Пользователь не брал эту вещь в аренду и не может оставить отзыв");
        }

        Comment comment = Comment.builder()
                .text(text)
                .created(LocalDateTime.now())
                .item(item)
                .author(author)
                .build();

        Comment saved = commentRepository.save(comment);
        return toCommentDto(saved);
    }

    private ItemDto toItemDtoWithExtra(Item item, List<Comment> comments) {
        LocalDateTime now = LocalDateTime.now();
        List<BookingStatus> activeStatuses = Arrays.asList(BookingStatus.APPROVED);

        Booking nextBooking = bookingRepository.findNextBookingForItem(item.getId(), now, activeStatuses);
        Booking lastBooking = bookingRepository.findLastBookingForItem(item.getId(), now);

        ItemDto dto = toItemDto(item);

        dto.setNextBookingStart(nextBooking != null ? nextBooking.getStart() : null);
        dto.setLastBookingEnd(lastBooking != null ? lastBooking.getEnd() : null);

        if (comments != null) {
            dto.setComments(comments.stream()
                    .map(ItemMapper::toCommentDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setComments(new ArrayList<>());
        }
        return dto;
    }
}