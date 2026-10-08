package com.formcrafter.auth.exception;

import com.formcrafter.auth.user.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Nested
    class HandleAppException {

        @Test
        void returnsStatusAndBodyFromAppException() {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/register");

            ResponseEntity<ExceptionResponse> response =
                    handler.handleAppException(new UserAlreadyExistsException(), request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getStatus()).isEqualTo(409);
            assertThat(response.getBody().getMessage()).isEqualTo("A user with this email already exists");
            assertThat(response.getBody().getPath()).isEqualTo("/api/auth/register");
        }
    }

    @Nested
    class HandleValidation {

        @Test
        void joinsFieldErrorsIntoMessage() throws Exception {
            Object target = new Object();
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "registerRequest");
            bindingResult.addError(new FieldError("registerRequest", "email", "Email is required"));
            bindingResult.addError(new FieldError("registerRequest", "password", "Password is required"));
            MethodArgumentNotValidException exception =
                    new MethodArgumentNotValidException(null, bindingResult);
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/auth/register");

            ResponseEntity<ExceptionResponse> response = handler.handleValidation(exception, request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage())
                    .contains("email: Email is required")
                    .contains("password: Password is required");
            assertThat(response.getBody().getPath()).isEqualTo("/api/auth/register");
        }
    }

    @Nested
    class HandleGeneric {

        @Test
        void returnsInternalServerErrorWithoutExposingDetails() {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users/me");

            ResponseEntity<ExceptionResponse> response =
                    handler.handleGeneric(new RuntimeException("boom"), request);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getMessage()).isEqualTo("An unexpected error occurred");
            assertThat(response.getBody().getPath()).isEqualTo("/api/users/me");
        }
    }
}
