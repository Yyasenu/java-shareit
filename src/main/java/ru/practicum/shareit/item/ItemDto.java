package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;
    @NotNull
    private Boolean available;
    private Long ownerId;
    private LocalDateTime nextBookingStart;
    private LocalDateTime lastBookingEnd;
    private List<CommentDto> comments;
}