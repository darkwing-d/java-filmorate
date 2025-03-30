package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    private Film createFilm(String name, String description, LocalDate releaseDate) {
        return Film.builder()
                .id(1L)
                .name(name)
                .duration(100)
                .releaseDate(releaseDate)
                .description(description)
                .build();
    }

    @Test
    void shouldNotValidateBlankName() {
        Film film = createFilm("", "description", LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateDescriptionMoreThan200Symbols() {
        Film film = createFilm("name", "1".repeat(201), LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(Size.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("description", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateDescription() {
        Film film = createFilm("name", "1".repeat(200), LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateReleaseDate() {
        Film film = createFilm("name", "description", LocalDate.of(1895, 12, 29));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidate() {
        Film film = createFilm("name", "description", LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateNullName() {
        Film film = createFilm(null, "dascription", LocalDate.now());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidatePositiveDuration() {
        Film film = createFilm("name", "description", LocalDate.now());
        film.setDuration(Duration.ofSeconds(100)); // Положительная длительность

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}
