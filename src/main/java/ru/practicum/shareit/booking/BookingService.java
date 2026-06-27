package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingUpdateRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking createBooking(long bookerId, BookingRequestDto dto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь-арендатор не найден"));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (dto.getStart() == null || dto.getEnd() == null) {
            throw new ValidationException("Даты начала и окончания обязательны");
        }
        if (!dto.getStart().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Дата начала должна быть в будущем");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }

        Booking booking = Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBooking(long bookingId, long userId, BookingUpdateRequestDto dto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new ValidationException("Только владелец вещи может подтверждать или отклонять бронирование");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Можно изменять только бронирования со статусом WAITING");
        }

        booking.setStatus(dto.getApproved() ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        long bookerId = booking.getBooker().getId();
        long ownerId = booking.getItem().getOwner().getId();

        if (user.getId().equals(bookerId) || user.getId().equals(ownerId)) {
            return booking;
        } else {
            throw new ValidationException("Доступ к бронированию есть только у арендатора или владельца вещи");
        }
    }

    public List<Booking> getUserBookings(long userId, ru.practicum.shareit.booking.dto.BookingState state) {
        List<Booking> all = new ArrayList<>(bookingRepository.findByBookerIdOrderByStartDesc(userId));
        return filterByState(all, state);
    }

    public List<Booking> getOwnerBookings(long ownerId, ru.practicum.shareit.booking.dto.BookingState state) {
        List<Booking> all = new ArrayList<>(bookingRepository.findByOwnerIdOrderByStartDesc(ownerId));
        return filterByState(all, state);
    }

    private List<Booking> filterByState(List<Booking> bookings, ru.practicum.shareit.booking.dto.BookingState state) {
        LocalDateTime now = LocalDateTime.now();

        return bookings.stream().filter(b -> {
            switch (state) {
                case ALL:
                    return true;
                case CURRENT:
                    return b.getStart().isBefore(now) && b.getEnd().isAfter(now);
                case PAST:
                    return b.getEnd().isBefore(now);
                case FUTURE:
                    return b.getStart().isAfter(now);
                case WAITING:
                    return b.getStatus() == BookingStatus.WAITING;
                case REJECTED:
                    return b.getStatus() == BookingStatus.REJECTED;
                default:
                    return false;
            }
        }).toList();
    }
}