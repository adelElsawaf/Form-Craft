package com.formcrafter.auth.auth.mappers;

import com.formcrafter.auth.auth.dtos.google_integrations.GoogleUserDTO;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthMapper {

    public CreateUserRequest toCreateUserRequest(RegisterRequest request) {
        CreateIdentityRequest identity = null;

        if (StringUtils.hasText(request.getPassword())) {
            identity = CreateIdentityRequest.builder()
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(request.getEmail())
                    .secret(request.getPassword())
                    .build();
        } else if (StringUtils.hasText(request.getGoogleId())) {
            identity = CreateIdentityRequest.builder()
                    .provider(AuthProvider.GOOGLE)
                    .providerUserId(request.getGoogleId())
                    .build();
        }

        return CreateUserRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .identity(identity)
                .build();
    }

    public CreateUserRequest toCreateUserRequest(GoogleUserDTO googleUser) {
        return CreateUserRequest.builder()
                .firstName(StringUtils.hasText(googleUser.firstName()) ? googleUser.firstName() : "Google")
                .lastName(StringUtils.hasText(googleUser.lastName()) ? googleUser.lastName() : "User")
                .email(googleUser.email())
                .identity(CreateIdentityRequest.builder()
                        .provider(AuthProvider.GOOGLE)
                        .providerUserId(googleUser.googleId())
                        .build())
                .build();
    }

    public AuthUserDTO toAuthUserDTO(UserDTO user) {
        return AuthUserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
