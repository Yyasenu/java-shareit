package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentResponseDTO;

@UtilityClass
public class CommentMapper {
    public CommentResponseDTO toDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}