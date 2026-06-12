package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {

    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}