package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotBlank(message = "Текст отзыва не может быть пустым")
    private String text;
}