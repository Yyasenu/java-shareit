package ru.practicum.shareit.booking.dto;

import ch.qos.logback.core.status.Status;
import lombok.Data;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {

    private Long id;

    private LocalDateTime bookingStart;

    private LocalDateTime bookingEnd;

    private ItemDto item;

    private UserDto booker;

    private Status status;
}