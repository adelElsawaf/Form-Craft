package com.formcrafter.auth.auth.documentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag(
        name = "Authentication",
        description = """
                Register, log in (email/password or Google), and log out of FormCraft.

                Successful register/login/Google responses set httpOnly `accessToken` and `refreshToken` cookies. \
                Logout clears those cookies. Tokens are never returned in the JSON body.
                """
)
public @interface AuthControllerDocumentation {
}
