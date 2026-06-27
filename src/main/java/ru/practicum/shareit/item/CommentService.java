package ru.practicum.shareit.item;

public interface CommentService {

    CommentResponseDto createComment(long userId, long itemId, CommentDto commentDto);
}