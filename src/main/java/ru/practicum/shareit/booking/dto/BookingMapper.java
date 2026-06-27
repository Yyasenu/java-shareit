package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        Item item = booking.getItem();
        User booker = booking.getBooker();

        ItemDto itemDto = null;
        if (item != null) {
            itemDto = ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .build();
        }

        UserDto bookerDto = null;
        if (booker != null) {
            bookerDto = UserDto.builder()
                    .id(booker.getId())
                    .name(booker.getName())
                    .email(booker.getEmail())
                    .build();
        }

        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemDto)
                .booker(bookerDto)
                .status(booking.getStatus())
                .build();
    }
}