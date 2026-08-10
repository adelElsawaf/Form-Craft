package com.formcrafter.auth.auth.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(
        name = "RegisterRequest",
        description = "Payload to create a new user account with email and password."
)
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Schema(description = "User's first name", example = "Ada", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "User's last name", example = "Lovelace", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    @Schema(description = "Unique email address used as the account identifier", example = "ada@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(description = "Account password (min 8 characters)", example = "securePass1", minLength = 8, requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
