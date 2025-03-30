package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReleaseDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReleaseDate {
    String message() default "Дата релиза должна быть не ранее 28.12.1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}