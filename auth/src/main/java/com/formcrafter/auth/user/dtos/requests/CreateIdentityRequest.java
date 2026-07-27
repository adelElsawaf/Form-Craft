package com.formcrafter.auth.user.dtos.requests;

import com.formcrafter.auth.user_identity.enums.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIdentityRequest {
    private AuthProvider provider;
    private String providerUserId;
    private String secret;
}
