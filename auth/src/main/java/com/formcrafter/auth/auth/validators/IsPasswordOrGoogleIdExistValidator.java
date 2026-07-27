package com.formcrafter.auth.auth.validators;

import com.formcrafter.auth.auth.annotations.IsPasswordOrGoogleIdExist;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsPasswordOrGoogleIdExistValidator implements ConstraintValidator<IsPasswordOrGoogleIdExist, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return false;
        }

        boolean hasPassword = request.getPassword() != null && !request.getPassword().isBlank();
        boolean hasGoogleId = request.getGoogleId() != null && !request.getGoogleId().isBlank();

        return hasPassword || hasGoogleId;
    }
}
