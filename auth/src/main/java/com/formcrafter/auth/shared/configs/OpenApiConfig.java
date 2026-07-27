package com.formcrafter.auth.shared.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String ACCESS_TOKEN_COOKIE = "accessTokenCookie";

    @Bean
    public OpenAPI formCraftAuthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FormCraft Auth API")
                        .version("v1")
                        .description("""
                                Authentication and user identity API for FormCraft.

                                Access and refresh tokens are issued as httpOnly cookies \
                                (`accessToken`, `refreshToken`) on successful registration. \
                                Protected endpoints expect a valid `accessToken` cookie.
                                """))
                .components(new Components()
                        .addSecuritySchemes(ACCESS_TOKEN_COOKIE, new SecurityScheme()
                                .name("accessToken")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .description("JWT access token stored in the httpOnly `accessToken` cookie.")));
    }
}
