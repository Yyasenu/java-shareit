package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

import static ru.practicum.shareit.user.UserMapper.toUser;
import static ru.practicum.shareit.user.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userStorage.create(toUser(userDto));
        return toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(long id) {
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return toUserDto(user.get());
    }

    @Override
    public UserDto updateUser(long id, UserUpdateDto userUpdateDto) {
        Optional<User> user = userStorage.getUserById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User userUpdated = userStorage.update(id, toUser(userUpdateDto));
        return toUserDto(userUpdated);
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }
}