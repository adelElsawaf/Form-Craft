package com.formcrafter.auth.security.exceptions;

import com.formcrafter.auth.exception.AppException;
import org.springframework.http.HttpStatus;

public class UnauthenticatedException extends AppException {

    public UnauthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, "Authentication is required to access this resource");
    }
}
