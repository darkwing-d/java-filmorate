package ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user) throws DuplicatedDataException;

    User update(User user) throws NotFoundException;

    User getById(Long id) throws NotFoundException;

    boolean existsById(Long id);

    void delete(Long id);
}