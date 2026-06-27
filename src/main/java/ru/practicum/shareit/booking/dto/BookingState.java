package ru.practicum.shareit.booking.dto;

public enum BookingState {
    ALL("Все"),
    CURRENT("Текущие"),
    PAST("Завершенные"),
    FUTURE("Будущие"),
    WAITING("Ожидающие подтверждения"),
    REJECTED("Отклоненные");

    BookingState(String description) {
    }
}