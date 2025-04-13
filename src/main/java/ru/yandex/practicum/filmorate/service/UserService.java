package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final Map<Long, Set<Long>> friends = new HashMap<>();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (!userStorage.existsById(user.getId())) {
            throw new NotFoundException("Юзер с id " + user.getId() + " не найден");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
        friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Юзер с id " + userId + " не найден");
        }

        if (!userStorage.existsById(friendId)) {
            throw new NotFoundException("Юзер с id " + friendId + " не найден");
        }

        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
        }
        if (friends.containsKey(friendId)) {
            friends.get(friendId).remove(userId);
        }
    }

    public List<User> getFriends(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return friends.getOrDefault(userId, Collections.emptySet()).stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        Set<Long> friends1 = friends.getOrDefault(userId1, Collections.emptySet());
        Set<Long> friends2 = friends.getOrDefault(userId2, Collections.emptySet());

        return friends1.stream()
                .filter(friends2::contains)
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}