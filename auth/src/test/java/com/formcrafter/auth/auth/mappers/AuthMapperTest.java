package com.formcrafter.auth.auth.mappers;

import com.formcrafter.auth.auth.dtos.google_integrations.GoogleUserDTO;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthMapperTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String PASSWORD = "securePass1";
    private static final String GOOGLE_ID = "google-user-123";

    private final AuthMapper authMapper = new AuthMapper();

    @Nested
    class ToCreateUserRequestFromRegister {

        @Test
        void mapsRegisterRequestToEmailPasswordIdentity() {
            RegisterRequest request = sampleRegisterRequest();

            CreateUserRequest actual = authMapper.toCreateUserRequest(request);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    CreateUserRequest.builder()
                            .firstName(FIRST_NAME)
                            .lastName(LAST_NAME)
                            .email(EMAIL)
                            .identity(CreateIdentityRequest.builder()
                                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                                    .providerUserId(EMAIL)
                                    .secret(PASSWORD)
                                    .build())
                            .build()
            );
        }
    }

    @Nested
    class ToCreateUserRequestFromGoogle {

        @Test
        void whenNamesPresent_mapsGoogleUserWithGoogleIdentity() {
            GoogleUserDTO googleUser = new GoogleUserDTO(GOOGLE_ID, EMAIL, FIRST_NAME, LAST_NAME);

            CreateUserRequest actual = authMapper.toCreateUserRequest(googleUser);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    CreateUserRequest.builder()
                            .firstName(FIRST_NAME)
                            .lastName(LAST_NAME)
                            .email(EMAIL)
                            .identity(CreateIdentityRequest.builder()
                                    .provider(AuthProvider.GOOGLE)
                                    .providerUserId(GOOGLE_ID)
                                    .build())
                            .build()
            );
        }

        @Test
        void whenNamesBlank_usesDefaultNames() {
            GoogleUserDTO googleUser = new GoogleUserDTO(GOOGLE_ID, EMAIL, "  ", null);

            CreateUserRequest actual = authMapper.toCreateUserRequest(googleUser);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    CreateUserRequest.builder()
                            .firstName("Google")
                            .lastName("User")
                            .email(EMAIL)
                            .identity(CreateIdentityRequest.builder()
                                    .provider(AuthProvider.GOOGLE)
                                    .providerUserId(GOOGLE_ID)
                                    .build())
                            .build()
            );
        }
    }

    @Nested
    class ToAuthUserDTO {

        @Test
        void copiesUserFields() {
            UserDTO user = UserDTO.builder()
                    .id(USER_ID)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .email(EMAIL)
                    .build();

            AuthUserDTO actual = authMapper.toAuthUserDTO(user);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    AuthUserDTO.builder()
                            .id(USER_ID)
                            .firstName(FIRST_NAME)
                            .lastName(LAST_NAME)
                            .email(EMAIL)
                            .build()
            );
        }
    }

    private static RegisterRequest sampleRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        return request;
    }
}
