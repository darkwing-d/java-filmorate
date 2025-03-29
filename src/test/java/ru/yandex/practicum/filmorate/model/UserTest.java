package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    static User getUser() {
        return User.builder()
                .id(1L)
                .login("login")
                .email("kakoitoemail@chtototam.com")
                .name("name")
                .birthday(LocalDate.of(2000, 5, 5))
                .build();
    }

    @Test
    void shouldValidate() {
        User user = getUser();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateEmailWithoutDog() {
        User user = getUser();
        user.setEmail("chtototam.22222");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateBlankEmail() {
        User user = getUser();
        user.setEmail("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(NotBlank.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateEmailWithMisplacedAt() {
        User user = getUser();
        user.setEmail("@chtototam.22222");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(Email.class, violation.getConstraintDescriptor().getAnnotation().annotationType());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateLoginWithSpaces() {
        User user = getUser();
        user.setLogin("login with spaces");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldNotValidateBirthdayInFuture() {
        User user = getUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("birthday", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateEmptyLogin() {
        User user = getUser();
        user.setLogin("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateInvalidEmail() {
        User user = getUser();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void shouldNotValidateEmptyName() {
        User user = getUser();
        user.setName("");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldValidateCorrectUser() {
        User user = getUser();
        user.setLogin("validLogin");
        user.setEmail("valid.email@example.com");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.now().minusYears(25));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}