package com.formcrafter.auth.auth.annotations;

import com.formcrafter.auth.auth.validators.IsPasswordOrGoogleIdExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsPasswordOrGoogleIdExistValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsPasswordOrGoogleIdExist {
    String message() default "Either password or googleId must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}