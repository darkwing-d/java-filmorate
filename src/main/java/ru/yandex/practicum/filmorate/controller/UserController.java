package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();
    private long nextId = 1;

    //Get - получение списка всех пользователей
    @GetMapping
    public Collection<User> findAll() {
        log.info("Получение списка всех пользователей");
        return users.values();
    }

    //Post - создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Создание пользователя с email: {}", user.getEmail());

        if (usersByEmail.containsKey(user.getEmail())) {
            log.error("Ошибка при создании пользователя: email {} уже используется", user.getEmail());
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        // Установка ID
        user.setId(getNextId());

        // Сохранение пользователя
        users.put(user.getId(), user);
        usersByEmail.put(user.getEmail(), user);

        log.info("Пользователь с id {} успешно создан", user.getId());
        return user;
    }

    //Put - обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя с id: {}", newUser.getId());

        User oldUser = users.get(newUser.getId());
        if (oldUser == null) {
            log.error("Ошибка при обновлении пользователя: пользователь с id = {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail())) {
            if (usersByEmail.containsKey(newUser.getEmail())) {
                log.error("Ошибка при обновлении пользователя: email {} уже используется", newUser.getEmail());
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            usersByEmail.remove(oldUser.getEmail());
            oldUser.setEmail(newUser.getEmail());
            usersByEmail.put(newUser.getEmail(), oldUser);
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }

        log.info("Пользователь с id {} успешно обновлен", oldUser.getId());
        return oldUser;
    }

    private long getNextId() {
        return nextId++;
    }
}
