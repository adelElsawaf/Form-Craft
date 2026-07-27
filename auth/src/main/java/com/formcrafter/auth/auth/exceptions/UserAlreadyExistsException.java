package com.formcrafter.auth.auth.exceptions;

import com.formcrafter.auth.exception.AppException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends AppException {

    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "A user with this email already exists");
    }

    public UserAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
