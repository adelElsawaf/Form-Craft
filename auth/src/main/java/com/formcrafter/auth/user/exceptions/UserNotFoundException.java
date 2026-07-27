package com.formcrafter.auth.user.exceptions;

import com.formcrafter.auth.exception.AppException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {

    public UserNotFoundException(String email) {
        super(HttpStatus.NOT_FOUND, "User not found with email: " + email);
    }

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "User not found with id: " + id);
    }
}
