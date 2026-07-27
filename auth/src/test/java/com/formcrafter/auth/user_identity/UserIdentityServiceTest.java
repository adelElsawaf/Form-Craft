package com.formcrafter.auth.user_identity;

import com.formcrafter.auth.user.UserEntity;
import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Learning benchmark for UserIdentityService unit tests.
 *
 * Pattern:
 * 1. Name: method_whenCondition_expectedResult
 * 2. Arrange → Act → Assert
 * 3. Mock the DB boundary (repository); keep real BCrypt
 * 4. Assert the full result the method owns
 *
 * buildIdentity has real logic → worth thorough unit tests.
 * exists/find are thin repo pass-throughs → one stubbing example each (SQL belongs in @DataJpaTest later).
 */
@ExtendWith(MockitoExtension.class)
class UserIdentityServiceTest {

    private static final String EMAIL = "ada@example.com";
    private static final String PLAIN_PASSWORD = "plain-password";
    private static final String GOOGLE_ID = "google-user-123";

    @Mock
    private UserIdentityRepository userIdentityRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserIdentityService userIdentityService;

    @BeforeEach
    void setUp() {
        userIdentityService = new UserIdentityService(userIdentityRepository, passwordEncoder);
    }

    @Nested
    class BuildIdentity {

        @Test
        void buildIdentity_forEmailPassword_copiesFieldsAndHashesPlaintextSecret() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(EMAIL)
                    .secret(PLAIN_PASSWORD)
                    .build();

            UserIdentityEntity identity = userIdentityService.buildIdentity(user, request);

            assertAll(
                    () -> assertSame(user, identity.getUser()),
                    () -> assertEquals(AuthProvider.EMAIL_AND_PASSWORD, identity.getProvider()),
                    () -> assertEquals(EMAIL, identity.getProviderUserId()),
                    () -> assertNotNull(identity.getCredentialSecret()),
                    () -> assertNotEquals(PLAIN_PASSWORD, identity.getCredentialSecret()),
                    () -> assertTrue(passwordEncoder.matches(PLAIN_PASSWORD, identity.getCredentialSecret()))
            );
        }

        @Test
        void buildIdentity_forGoogle_copiesFieldsAndLeavesCredentialSecretNull() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.GOOGLE)
                    .providerUserId(GOOGLE_ID)
                    .secret(null)
                    .build();

            UserIdentityEntity identity = userIdentityService.buildIdentity(user, request);

            assertAll(
                    () -> assertSame(user, identity.getUser()),
                    () -> assertEquals(AuthProvider.GOOGLE, identity.getProvider()),
                    () -> assertEquals(GOOGLE_ID, identity.getProviderUserId()),
                    () -> assertNull(identity.getCredentialSecret())
            );
        }

        @Test
        void buildIdentity_forEmailPasswordWithBlankSecret_leavesCredentialSecretNull() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(EMAIL)
                    .secret("   ")
                    .build();

            UserIdentityEntity identity = userIdentityService.buildIdentity(user, request);

            assertAll(
                    () -> assertSame(user, identity.getUser()),
                    () -> assertEquals(AuthProvider.EMAIL_AND_PASSWORD, identity.getProvider()),
                    () -> assertEquals(EMAIL, identity.getProviderUserId()),
                    () -> assertNull(identity.getCredentialSecret())
            );
        }
    }

    /**
     * Thin service methods: unit tests only prove delegation.
     * Case-insensitive SQL behavior is tested later with @DataJpaTest on the repository.
     */
    @Nested
    class RepositoryDelegation {

        @Test
        void existsByProviderAndProviderUserId_whenRepositoryReturnsTrue_returnsTrue() {
            when(userIdentityRepository.existsByProviderAndProviderUserIdIgnoreCase(
                    AuthProvider.GOOGLE, GOOGLE_ID
            )).thenReturn(true);

            boolean exists = userIdentityService.existsByProviderAndProviderUserId(
                    AuthProvider.GOOGLE, GOOGLE_ID
            );

            assertTrue(exists);
            verify(userIdentityRepository).existsByProviderAndProviderUserIdIgnoreCase(
                    AuthProvider.GOOGLE, GOOGLE_ID
            );
        }

        @Test
        void existsByProviderAndProviderUserId_whenRepositoryReturnsFalse_returnsFalse() {
            when(userIdentityRepository.existsByProviderAndProviderUserIdIgnoreCase(
                    AuthProvider.EMAIL_AND_PASSWORD, EMAIL
            )).thenReturn(false);

            boolean exists = userIdentityService.existsByProviderAndProviderUserId(
                    AuthProvider.EMAIL_AND_PASSWORD, EMAIL
            );

            assertFalse(exists);
        }

        @Test
        void findByProviderAndProviderUserId_whenPresent_returnsIdentity() {
            UserIdentityEntity stored = UserIdentityEntity.builder()
                    .user(sampleUser())
                    .provider(AuthProvider.GOOGLE)
                    .providerUserId(GOOGLE_ID)
                    .build();

            when(userIdentityRepository.findByProviderAndProviderUserIdIgnoreCase(
                    AuthProvider.GOOGLE, GOOGLE_ID
            )).thenReturn(Optional.of(stored));

            Optional<UserIdentityEntity> result = userIdentityService.findByProviderAndProviderUserId(
                    AuthProvider.GOOGLE, GOOGLE_ID
            );

            assertTrue(result.isPresent());
            assertSame(stored, result.get());
        }

        @Test
        void findByProviderAndProviderUserId_whenAbsent_returnsEmpty() {
            when(userIdentityRepository.findByProviderAndProviderUserIdIgnoreCase(
                    AuthProvider.GOOGLE, GOOGLE_ID
            )).thenReturn(Optional.empty());

            Optional<UserIdentityEntity> result = userIdentityService.findByProviderAndProviderUserId(
                    AuthProvider.GOOGLE, GOOGLE_ID
            );

            assertTrue(result.isEmpty());
        }
    }

    private static UserEntity sampleUser() {
        return UserEntity.builder()
                .email(EMAIL)
                .firstName("Ada")
                .lastName("Lovelace")
                .build();
    }
}
