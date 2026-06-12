package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto dto);

    Collection<UserDto> getAll();

    UserDto getUserById(long id);

    UserDto updateUser(long id, UserUpdateDto dto);

    void deleteUser(long id);
}