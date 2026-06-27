/*package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private long nextId = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User create(User user) {
        boolean emailExists = isEmailExist(user.getEmail());
        if (emailExists) {
            throw new ConflictException("Электронная почта уже существует");
        }
        Long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(long id, User user) {
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            boolean emailExists = users.values().stream()
                    .filter(u -> u.getId() != null && !u.getId().equals(id))
                    .anyMatch(u -> {
                        String existingEmail = u.getEmail();
                        if (existingEmail == null) return false;
                        return existingEmail.equalsIgnoreCase(user.getEmail());
                    });

            if (emailExists) {
                throw new ConflictException("Электронная почта уже существует");
            }
        }

        User userUpdated = users.get(id);
        if (userUpdated == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            userUpdated.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            userUpdated.setEmail(user.getEmail());
        }

        return userUpdated;
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    private boolean isEmailExist(String email) {
        return users.values().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(email));
    }

    private long getNextId() {
        return nextId++;
    }
}
 */