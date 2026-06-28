package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    @NotNull(message = "Комментарий обязательно")
    private String text;
}