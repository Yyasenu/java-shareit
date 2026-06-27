package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    BookingResponseDto mapToBookingResponseDto(Booking booking);

    List<BookingResponseDto> mapToBookingListDto(List<Booking> bookings);
}