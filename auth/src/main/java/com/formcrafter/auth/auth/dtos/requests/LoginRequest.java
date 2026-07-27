package com.formcrafter.auth.auth.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoginRequest", description = "Credentials used to authenticate an existing user.")
public class LoginRequest {
    @Email
    @NotBlank
    @Schema(description = "Account email address", example = "ada@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank
    @Schema(description = "Account password", example = "securePass1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
