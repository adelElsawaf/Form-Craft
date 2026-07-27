package com.formcrafter.auth.auth.documentation.apis;

import com.formcrafter.auth.exception.ExceptionResponse;
import com.formcrafter.auth.shared.configs.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
        summary = "Log out",
        description = """
                Ends the current session by expiring the `accessToken` and `refreshToken` cookies \
                (`Max-Age=0`). Requires a valid authenticated session.
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Logged out successfully. Auth cookies are cleared.",
                headers = {
                        @Header(
                                name = HttpHeaders.SET_COOKIE,
                                description = "Expires the `accessToken` and `refreshToken` cookies."
                        )
                }
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
@SecurityRequirement(name = OpenApiConfig.ACCESS_TOKEN_COOKIE)
public @interface LogoutDocumentation {
}
