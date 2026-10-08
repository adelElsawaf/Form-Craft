package com.formcrafter.auth.user.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class UserExceptionsTest {

    @Test
    void userAlreadyExists_defaultMessage() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException();

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getMessage()).isEqualTo("A user with this email already exists");
    }

    @Test
    void userAlreadyExists_customMessage() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("custom");

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getMessage()).isEqualTo("custom");
    }

    @Test
    void userNotFound_byEmailAndId() {
        assertThat(new UserNotFoundException("a@b.com").getMessage())
                .isEqualTo("User not found with email: a@b.com");
        assertThat(new UserNotFoundException(9L).getMessage())
                .isEqualTo("User not found with id: 9");
        assertThat(new UserNotFoundException(9L).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
