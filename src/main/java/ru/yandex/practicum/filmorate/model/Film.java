package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ValidReleaseDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private long id;

    @NotBlank
    private String name;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    @Size(max = 200)
    private String description;

    @Builder
    public Film(long id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}