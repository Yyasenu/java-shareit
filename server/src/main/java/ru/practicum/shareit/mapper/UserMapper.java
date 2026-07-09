package ru.practicum.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRequestDTO;
import ru.practicum.shareit.user.UserResponseDTO;

@UtilityClass
public class UserMapper {
    public User toUser(UserRequestDTO dto) {
        return User.builder()
                .id(null)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserResponseDTO toResponseDto(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}