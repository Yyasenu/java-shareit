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

    private final UserMapper userMapper;

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
            throw new NotFoundException("User not found");
        }
        return toUserDto(user.get());
    }

    @Override
    public UserDto updateUser(long id, UserUpdateDto dto) {
        Optional<User> optionalUser = userStorage.getUserById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        User existingUser = optionalUser.get();

        if (dto.getName() != null) {
            existingUser.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }

        User updated = userStorage.update(id, existingUser);

        return UserMapper.toUserDto(updated);
    }

    @Override
    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }
}