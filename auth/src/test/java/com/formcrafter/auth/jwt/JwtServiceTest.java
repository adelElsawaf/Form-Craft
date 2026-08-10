package com.formcrafter.auth.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String USERNAME = "adel_elsawaf@example.com";
    private static final Duration ACCESS_EXPIRATION = Duration.ofMinutes(15);
    private static final Duration REFRESH_EXPIRATION = Duration.ofDays(7);

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        String secret = Base64.getEncoder().encodeToString(
                "formcraft-test-jwt-secret-key-32b!".getBytes()
        );
        jwtService = new JwtService(new JwtProperties(secret, ACCESS_EXPIRATION, REFRESH_EXPIRATION));
    }

    @Nested
    class GenerateAndValidate {

        @Test
        void accessToken_isValidForAccessTypeAndUsername() {
            String token = jwtService.generateAccessToken(USERNAME);

            assertThat(jwtService.extractUsername(token)).isEqualTo(USERNAME);
            assertThat(jwtService.isTokenValid(token, USERNAME, TokenType.ACCESS)).isTrue();
            assertThat(jwtService.isTokenValid(token, USERNAME, TokenType.REFRESH)).isFalse();
            assertThat(jwtService.isTokenValid(token, "other@example.com", TokenType.ACCESS)).isFalse();
        }

        @Test
        void refreshToken_isValidForRefreshTypeAndUsername() {
            String token = jwtService.generateRefreshToken(USERNAME);

            assertThat(jwtService.extractUsername(token)).isEqualTo(USERNAME);
            assertThat(jwtService.isTokenValid(token, USERNAME, TokenType.REFRESH)).isTrue();
            assertThat(jwtService.isTokenValid(token, USERNAME, TokenType.ACCESS)).isFalse();
        }

        @Test
        void malformedToken_isInvalid() {
            assertThat(jwtService.isTokenValid("not-a-jwt", USERNAME, TokenType.ACCESS)).isFalse();
        }

        @Test
        void expiredToken_isInvalid() throws Exception {
            JwtService shortLived = new JwtService(new JwtProperties(
                    Base64.getEncoder().encodeToString("formcraft-test-jwt-secret-key-32b!".getBytes()),
                    Duration.ofMillis(1),
                    REFRESH_EXPIRATION
            ));
            String token = shortLived.generateAccessToken(USERNAME);
            Thread.sleep(50);

            assertThat(shortLived.isTokenValid(token, USERNAME, TokenType.ACCESS)).isFalse();
        }
    }

    @Nested
    class ExpirationAccessors {

        @Test
        void returnsConfiguredDurations() {
            assertThat(jwtService.getAccessTokenExpiration()).isEqualTo(ACCESS_EXPIRATION);
            assertThat(jwtService.getRefreshTokenExpiration()).isEqualTo(REFRESH_EXPIRATION);
        }
    }
}
