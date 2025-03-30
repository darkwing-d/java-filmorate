package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    //Get - для получения списка всех фильмов
    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение всех фильмов. Всего фильмов: {}", films.size());
        return films.values();
    }

    //Post - для добавления фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    //Put - обновление фильма
    @PutMapping("/{id}")
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Запрос на обновление фильма с id={}", newFilm.getId());
        Film oldFilm = films.get(newFilm.getId());

        if (oldFilm == null) {
            log.warn("Фильм с указанным id не найден: {}", newFilm.getId());
            throw new ResourceNotFoundException("Фильм с указанным id не найден: " + newFilm.getId());
        }

        // Обновление полей
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
            log.info("Обновлено название фильма с id={}: {}", newFilm.getId(), newFilm.getName());
        }

        // Валидация обновленных полей
        validateFilm(newFilm);

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }

        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
            log.info("Обновлено описание фильма с id={}: {}", newFilm.getId(), newFilm.getDescription());
        }

        return oldFilm;
    }

    // валидация фильма
    private void validateFilm(Film film) {
        if (film.getReleaseDate() == null) {
            log.warn("Дата релиза фильма не может быть пуста: {}", film);
            throw new ValidationException("Нужно указать дату релиза фильма");
        }

        if (film.getDuration() == null) {
            log.warn("Продолжительность фильма не может быть пуста: {}", film);
            throw new ValidationException("Необходимо указать продолжительность фильма");
        } else if (film.getDuration().isNegative()) {
            log.warn("Продолжительность фильма не может быть отрицательным числом: {}", film);
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом");
        }
    }

    private long getNextId() {
        return films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0) + 1;
    }
}
