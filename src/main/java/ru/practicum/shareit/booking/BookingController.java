package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") long bookerId,
            @RequestBody BookingRequestDto dto) {

        Booking booking = bookingService.createBooking(bookerId, dto);
        return BookingMapper.toBookingDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody BookingUpdateRequestDto dto) {

        Booking booking = bookingService.updateBooking(bookingId, userId, dto);
        return BookingMapper.toBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId) {

        Booking booking = bookingService.getBookingById(bookingId, userId);
        return BookingMapper.toBookingDto(booking);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.getUserBookings(userId, state).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(defaultValue = "ALL") BookingState state) {

        return bookingService.getOwnerBookings(ownerId, state).stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}