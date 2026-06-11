package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private Map<Long, User> users;
    private Map<String, Long> emailsIndex;
    private Long nextID;

    private void reset() {
        this.users = new ConcurrentHashMap<>();
        this.emailsIndex = new ConcurrentHashMap<>();
        this.nextID = 1L;
    }

    @Override
    public UserDto create(UserDto dto) {
        if (emailsIndex.containsKey(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким email уже существует");
        }

        User user = userMapper.toModel(dto);
        user.setId(nextID++);

        users.put(user.getId(), user);
        emailsIndex.put(user.getEmail(), user.getId());

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id " + id + " не найден");
        }
        return userMapper.toDto(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }
        return user;
    }

    @Override
    public UserDto update(UserDto dto) {
        Long id = dto.getId();
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не может быть null");
        }

        User existing = users.get(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        String oldEmail = existing.getEmail();
        String newEmail = dto.getEmail();

        if (newEmail != null && !newEmail.equals(oldEmail) && emailsIndex.containsKey(newEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким email уже существует");
        }

        existing.setName(dto.getName());
        existing.setEmail(newEmail);

        if (oldEmail != null) {
            emailsIndex.remove(oldEmail);
        }
        if (newEmail != null) {
            emailsIndex.put(newEmail, existing.getId());
        }

        return userMapper.toDto(existing);
    }

    @Override
    public void delete(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден для удаления");
        }
        users.remove(id);
        emailsIndex.remove(user.getEmail());
    }
}