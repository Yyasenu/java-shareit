package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDTOClient {
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;
}
