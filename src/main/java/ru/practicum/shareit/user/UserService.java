package ru.practicum.shareit.user;

public interface UserService {
    UserDto create(UserDto dto);

    UserDto getById(Long id);

    User getUserById(Long id);

    UserDto update(UserDto dto);

    void delete(Long id);
}