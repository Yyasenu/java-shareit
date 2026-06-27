package ru.practicum.shareit.item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner() != null ? item.getOwner().getId() : null)
                .nextBookingStart(null)
                .lastBookingEnd(null)
                .comments(null)
                .build();
    }

    public static ItemUpdateDto toItemUpdateDto(Item item) {
        return ItemUpdateDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static Item toItem(ItemUpdateDto itemUpdateDto) {
        return Item.builder()
                .name(itemUpdateDto.getName())
                .description(itemUpdateDto.getDescription())
                .available(itemUpdateDto.getAvailable())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) return null;
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor() != null ? comment.getAuthor().getName() : null)
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}