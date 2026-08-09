package com.formcrafter.auth.user_identity;

import com.formcrafter.auth.user.UserEntity;
import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserIdentityServiceTest {

    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String PLAIN_SECRET = "plain-secret";
    private static final String GOOGLE_ID = "google-user-123";

    @Mock
    private UserIdentityRepository userIdentityRepository;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserIdentityService userIdentityService;

    @Nested
    class FindByProviderAndProviderUserId {

        @Test
        void whenPresent_returnsIdentity() {
            UserIdentityEntity stored = sampleGoogleIdentity(sampleUser());

            when(userIdentityRepository.findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(Optional.of(stored));

            Optional<UserIdentityEntity> actual = userIdentityService
                    .findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).containsSame(stored);
            verify(userIdentityRepository)
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);
        }

        @Test
        void whenNotPresent_returnsEmpty() {
            when(userIdentityRepository.findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(Optional.empty());

            Optional<UserIdentityEntity> actual = userIdentityService
                    .findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isEmpty();
            verify(userIdentityRepository)
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);
        }
    }

    @Nested
    class ExistsByProviderAndProviderUserId {

        @Test
        void whenPresent_returnsTrue() {
            when(userIdentityRepository.existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(true);

            boolean actual = userIdentityService
                    .existsByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isTrue();
            verify(userIdentityRepository)
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);
        }

        @Test
        void whenNotPresent_returnsFalse() {
            when(userIdentityRepository.existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(false);

            boolean actual = userIdentityService
                    .existsByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isFalse();
            verify(userIdentityRepository)
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);
        }
    }

    @Nested
    class BuildIdentity {

        @Test
        void whenEmailAndPasswordAndSecretPresent_returnsIdentityWithHashedSecret() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(EMAIL)
                    .secret(PLAIN_SECRET)
                    .build();

            UserIdentityEntity actual = userIdentityService.buildIdentity(user, request);

            assertThat(actual.getUser()).isSameAs(user);
            assertThat(actual.getProvider()).isEqualTo(AuthProvider.EMAIL_AND_PASSWORD);
            assertThat(actual.getProviderUserId()).isEqualTo(EMAIL);
            assertThat(actual.getCredentialSecret())
                    .isNotNull()
                    .isNotEqualTo(PLAIN_SECRET);
            assertThat(passwordEncoder.matches(PLAIN_SECRET, actual.getCredentialSecret())).isTrue();
        }

        @Test
        void whenEmailAndPasswordAndSecretMissing_returnsIdentityWithNullSecret() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(EMAIL)
                    .secret(null)
                    .build();

            UserIdentityEntity expected = UserIdentityEntity.builder()
                    .user(user)
                    .provider(AuthProvider.EMAIL_AND_PASSWORD)
                    .providerUserId(EMAIL)
                    .credentialSecret(null)
                    .build();

            UserIdentityEntity actual = userIdentityService.buildIdentity(user, request);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenGoogleProvider_returnsIdentityWithNullSecret() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.GOOGLE)
                    .providerUserId(GOOGLE_ID)
                    .secret(null)
                    .build();

            UserIdentityEntity expected = sampleGoogleIdentity(user);

            UserIdentityEntity actual = userIdentityService.buildIdentity(user, request);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenGoogleProviderAndSecretPresent_returnsIdentityWithNullSecret() {
            UserEntity user = sampleUser();
            CreateIdentityRequest request = CreateIdentityRequest.builder()
                    .provider(AuthProvider.GOOGLE)
                    .providerUserId(GOOGLE_ID)
                    .secret(PLAIN_SECRET)
                    .build();

            UserIdentityEntity expected = sampleGoogleIdentity(user);

            UserIdentityEntity actual = userIdentityService.buildIdentity(user, request);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    private static UserEntity sampleUser() {
        return UserEntity.builder()
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
    }

    private static UserIdentityEntity sampleGoogleIdentity(UserEntity user) {
        return UserIdentityEntity.builder()
                .user(user)
                .provider(AuthProvider.GOOGLE)
                .providerUserId(GOOGLE_ID)
                .credentialSecret(null)
                .build();
    }
}
