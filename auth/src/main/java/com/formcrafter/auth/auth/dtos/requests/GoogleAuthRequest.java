package com.formcrafter.auth.auth.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GoogleAuthRequest", description = "Google OAuth authorization code from the client callback.")
public record GoogleAuthRequest(
        @Schema(
                description = "Authorization code returned by Google after the user consents.",
                example = "4/0AeanS...",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String code
) {}
