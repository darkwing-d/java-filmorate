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

    private Long id;

    @NotBlank
    private String name;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @JsonIgnore
    private Duration duration;

    @Size(max = 200)
    private String description;

    @Builder
    public Film(Long id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = Duration.ofSeconds(duration);
    }

    @JsonProperty("duration")
    public long getDurationSeconds() {
        return duration != null ? duration.getSeconds() : 0;
    }

    @JsonProperty("duration")
    public void setDurationSeconds(long seconds) {
        this.duration = Duration.ofSeconds(seconds);
    }
}
