package com.formcrafter.auth.auth.dtos.google_integrations;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenDTO(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("id_token") String idToken
) {}
