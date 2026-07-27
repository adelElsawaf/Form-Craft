package com.formcrafter.auth.auth.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AuthUser", description = "Authenticated user profile returned after registration.")
public class AuthUserDTO {
    @Schema(description = "Unique user identifier", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "Ada")
    private String firstName;

    @Schema(description = "User's last name", example = "Lovelace")
    private String lastName;

    @Schema(description = "User's email address", example = "ada@example.com")
    private String email;
}
