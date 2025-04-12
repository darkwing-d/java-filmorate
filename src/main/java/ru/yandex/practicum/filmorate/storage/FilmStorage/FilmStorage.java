package ru.yandex.practicum.filmorate.storage.FilmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film film);

    Film getById(Long id);

    void delete(Long id);
}
