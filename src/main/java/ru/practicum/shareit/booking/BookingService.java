package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(long userId, BookingDto bookingDto);

    BookingResponseDto updateApprovalStatus(long bookingId, long ownerId, boolean approved);

    BookingResponseDto getBookingById(long bookingId, long userId);

    List<BookingResponseDto> getBookingsByBooker(long userId, BookingState state);

    List<BookingResponseDto> getBookingsByOwner(long userId, BookingState state);
}