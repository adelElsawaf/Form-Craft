package com.formcrafter.auth.auth.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthTokensDto {
    private String accessToken;
    private String refreshToken;
}