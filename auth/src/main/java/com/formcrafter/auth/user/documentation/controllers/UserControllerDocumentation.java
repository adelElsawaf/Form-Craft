package com.formcrafter.auth.user.documentation.controllers;

import com.formcrafter.auth.shared.configs.OpenApiConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        name = "Users",
        description = """
                Endpoints for the authenticated user's profile. \
                All operations require a valid `accessToken` cookie.
                """
)
@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_COOKIE)
public @interface UserControllerDocumentation {
}
