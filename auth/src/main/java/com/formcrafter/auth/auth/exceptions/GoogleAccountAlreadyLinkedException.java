package com.formcrafter.auth.auth.exceptions;

import com.formcrafter.auth.exception.AppException;
import org.springframework.http.HttpStatus;

public class GoogleAccountAlreadyLinkedException extends AppException {

    public GoogleAccountAlreadyLinkedException() {
        super(HttpStatus.CONFLICT, "This Google account is already linked to an existing user");
    }
}
