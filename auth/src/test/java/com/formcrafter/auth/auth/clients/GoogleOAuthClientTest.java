package com.formcrafter.auth.auth.clients;

import com.formcrafter.auth.auth.configs.GoogleAuthProperties;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleOAuthClientTest {

    private static final String CLIENT_ID = "client-id";
    private static final String CLIENT_SECRET = "client-secret";
    private static final String REDIRECT_URI = "http://localhost/callback";
    private static final String CODE = "auth-code";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GoogleAuthProperties properties;

    @InjectMocks
    private GoogleOAuthClient googleOAuthClient;

    @BeforeEach
    void stubProperties() {
        when(properties.getClientId()).thenReturn(CLIENT_ID);
        when(properties.getClientSecret()).thenReturn(CLIENT_SECRET);
        when(properties.getRedirectUri()).thenReturn(REDIRECT_URI);
    }

    @Nested
    class ExchangeCode {

        @Test
        void postsAuthorizationCodeAndReturnsTokenBody() {
            GoogleTokenDTO expected = new GoogleTokenDTO("access-token", "id-token");
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(GoogleTokenDTO.class)))
                    .thenReturn(ResponseEntity.ok(expected));

            GoogleTokenDTO actual = googleOAuthClient.exchangeCode(CODE);

            assertThat(actual).isEqualTo(expected);

            @SuppressWarnings("unchecked")
            ArgumentCaptor<HttpEntity<MultiValueMap<String, String>>> requestCaptor =
                    ArgumentCaptor.forClass(HttpEntity.class);
            verify(restTemplate).postForEntity(eq(TOKEN_URL), requestCaptor.capture(), eq(GoogleTokenDTO.class));

            HttpEntity<MultiValueMap<String, String>> request = requestCaptor.getValue();
            assertThat(request.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_FORM_URLENCODED);
            assertThat(request.getBody()).containsEntry("client_id", java.util.List.of(CLIENT_ID));
            assertThat(request.getBody()).containsEntry("client_secret", java.util.List.of(CLIENT_SECRET));
            assertThat(request.getBody()).containsEntry("code", java.util.List.of(CODE));
            assertThat(request.getBody()).containsEntry("redirect_uri", java.util.List.of(REDIRECT_URI));
            assertThat(request.getBody()).containsEntry("grant_type", java.util.List.of("authorization_code"));
        }

        @Test
        void whenResponseBodyNull_returnsNull() {
            when(restTemplate.postForEntity(eq(TOKEN_URL), any(HttpEntity.class), eq(GoogleTokenDTO.class)))
                    .thenReturn(ResponseEntity.ok(null));

            assertThat(googleOAuthClient.exchangeCode(CODE)).isNull();
        }
    }
}
