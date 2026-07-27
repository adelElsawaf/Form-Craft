package com.formcrafter.auth.auth.dtos.google_integrations;

public record GoogleUserDTO(
        String googleId,
        String email,
        String firstName,
        String lastName

) {}

