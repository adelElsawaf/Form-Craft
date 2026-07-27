package com.formcrafter.auth.auth.documentation.apis;

import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.exception.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Log in",
        description = """
                Authenticates an existing user with email and password.

                On success, the response body contains the user profile. Authentication tokens are \
                **not** returned in the JSON body; instead, httpOnly cookies are set:
                - `accessToken` — short-lived JWT for authenticated API calls
                - `refreshToken` — longer-lived JWT used to obtain new access tokens
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Login successful. `accessToken` and `refreshToken` cookies are set.",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = AuthUserDTO.class)
                ),
                headers = {
                        @Header(
                                name = HttpHeaders.SET_COOKIE,
                                description = "Sets httpOnly `accessToken` and `refreshToken` cookies (SameSite=Strict, path=/)."
                        )
                }
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation failed (missing or invalid email/password).",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ExceptionResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Invalid email or password.",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ExceptionResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "No user found for the given email.",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ExceptionResponse.class)
                )
        )
})
public @interface LoginDocumentation {
}
