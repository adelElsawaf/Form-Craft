package com.formcrafter.auth.user.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User", description = "Public user profile for the authenticated account.")
public class UserDTO {
    @Schema(description = "Unique user identifier", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "Ada")
    private String firstName;

    @Schema(description = "User's last name", example = "Lovelace")
    private String lastName;

    @Schema(description = "User's email address", example = "ada@example.com")
    private String email;
}
