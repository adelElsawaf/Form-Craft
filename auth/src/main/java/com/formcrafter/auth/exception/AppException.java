package com.formcrafter.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class AppException extends RuntimeException {

    private final HttpStatus status;

    protected AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ExceptionResponse toResponse(String path) {
        return ExceptionResponse.of(status, getMessage(), path);
    }
}
