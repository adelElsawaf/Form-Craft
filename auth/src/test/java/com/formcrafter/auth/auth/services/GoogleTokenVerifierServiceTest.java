package com.formcrafter.auth.auth.services;

import com.formcrafter.auth.auth.configs.GoogleAuthProperties;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleUserDTO;
import com.formcrafter.auth.auth.exceptions.InvalidCredentialsException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleTokenVerifierServiceTest {

    private static final String ID_TOKEN = "google-id-token";
    private static final String GOOGLE_ID = "google-user-123";
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";

    @Mock
    private GoogleAuthProperties properties;

    @Mock
    private GoogleIdTokenVerifier verifier;

    private GoogleTokenVerifierService googleTokenVerifierService;

    @BeforeEach
    void setUp() {
        googleTokenVerifierService = new GoogleTokenVerifierService(properties) {
            @Override
            GoogleIdTokenVerifier buildVerifier() {
                return verifier;
            }
        };
    }

    @Nested
    class Verify {

        @Test
        void whenTokenValid_mapsPayloadToGoogleUser() throws Exception {
            GoogleIdToken token = mock(GoogleIdToken.class);
            GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
            payload.setSubject(GOOGLE_ID);
            payload.setEmail(EMAIL);
            payload.set("given_name", FIRST_NAME);
            payload.set("family_name", LAST_NAME);

            when(verifier.verify(ID_TOKEN)).thenReturn(token);
            when(token.getPayload()).thenReturn(payload);

            GoogleUserDTO actual = googleTokenVerifierService.verify(ID_TOKEN);

            assertThat(actual).isEqualTo(new GoogleUserDTO(GOOGLE_ID, EMAIL, FIRST_NAME, LAST_NAME));
        }

        @Test
        void whenTokenNull_throwsInvalidCredentialsException() throws Exception {
            when(verifier.verify(ID_TOKEN)).thenReturn(null);

            assertThatThrownBy(() -> googleTokenVerifierService.verify(ID_TOKEN))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        void buildVerifier_createsAudienceScopedVerifier() {
            GoogleAuthProperties props = new GoogleAuthProperties();
            props.setClientId("google-client-id");

            GoogleIdTokenVerifier built = new GoogleTokenVerifierService(props).buildVerifier();

            assertThat(built).isNotNull();
        }
    }
}
