package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequestDto {
    @NotNull(message = "Дата начала не может быть пустой")
    @FutureOrPresent(message = "Дата начала должна быть не раньше текущего момента")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания не может быть пустой")
    @PastOrPresent(message = "Дата окончания должна быть не раньше даты начала")
    private LocalDateTime end;

    @NotNull(message = "ID вещи не может быть пустым")
    private Long itemId;
}