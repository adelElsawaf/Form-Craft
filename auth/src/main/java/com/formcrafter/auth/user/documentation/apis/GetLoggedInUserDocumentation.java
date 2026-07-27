package com.formcrafter.auth.user.documentation.apis;

import com.formcrafter.auth.exception.ExceptionResponse;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        summary = "Get current user",
        description = """
                Returns the profile of the currently authenticated user, resolved from the \
                `accessToken` cookie. Use this to hydrate the client session after login or page reload.
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Authenticated user profile.",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Missing or invalid authentication cookie.",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ExceptionResponse.class)
                )
        )
})
public @interface GetLoggedInUserDocumentation {
}
