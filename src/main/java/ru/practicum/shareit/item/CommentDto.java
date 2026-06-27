package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    @NotBlank(message = "Текст комментария обязателен")
    private String text;
}