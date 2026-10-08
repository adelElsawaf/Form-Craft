package com.formcrafter.auth.user_identity;

import com.formcrafter.auth.shared.config.PostgresTestcontainersConfig;
import com.formcrafter.auth.user.UserEntity;
import com.formcrafter.auth.user.UserRepository;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestcontainersConfig.class)
class UserIdentityRepositoryTest {

    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String GOOGLE_ID = "google-user-123";
    private static final String GOOGLE_ID_DIFFERENT_CASE = "GOOGLE-USER-123";
    private static final String ANOTHER_GOOGLE_ID = "another-google-id";

    @Autowired
    private UserIdentityRepository userIdentityRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = createUser(EMAIL);
    }

    @Nested
    class FindByProviderAndProviderUserIdIgnoreCase {

        @Test
        void whenExactMatch_returnsIdentity() {
            UserIdentityEntity expected = createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            Optional<UserIdentityEntity> actual = userIdentityRepository
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenCaseIsDifferent_returnsIdentity() {
            UserIdentityEntity expected = createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID_DIFFERENT_CASE);

            Optional<UserIdentityEntity> actual = userIdentityRepository
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenNotExist_returnsEmptyOptional() {
            Optional<UserIdentityEntity> actual = userIdentityRepository
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isEmpty();
        }

        @Test
        void whenWrongProvider_returnsEmptyOptional() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            Optional<UserIdentityEntity> actual = userIdentityRepository
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.EMAIL_AND_PASSWORD, GOOGLE_ID);

            assertThat(actual).isEmpty();
        }

        @Test
        void whenWrongProviderUserId_returnsEmptyOptional() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            Optional<UserIdentityEntity> actual = userIdentityRepository
                    .findByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, ANOTHER_GOOGLE_ID);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class ExistsByProviderAndProviderUserIdIgnoreCase {

        @Test
        void whenExactMatch_returnsTrue() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            boolean actual = userIdentityRepository
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isTrue();
        }

        @Test
        void whenCaseIsDifferent_returnsTrue() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID_DIFFERENT_CASE);

            boolean actual = userIdentityRepository
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isTrue();
        }

        @Test
        void whenNotExist_returnsFalse() {
            boolean actual = userIdentityRepository
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, GOOGLE_ID);

            assertThat(actual).isFalse();
        }

        @Test
        void whenWrongProvider_returnsFalse() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            boolean actual = userIdentityRepository
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.EMAIL_AND_PASSWORD, GOOGLE_ID);

            assertThat(actual).isFalse();
        }

        @Test
        void whenWrongProviderUserId_returnsFalse() {
            createUserIdentity(AuthProvider.GOOGLE, GOOGLE_ID);

            boolean actual = userIdentityRepository
                    .existsByProviderAndProviderUserIdIgnoreCase(AuthProvider.GOOGLE, ANOTHER_GOOGLE_ID);

            assertThat(actual).isFalse();
        }
    }

    private UserEntity createUser(String email) {
        return userRepository.save(UserEntity.builder()
                .email(email)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build());
    }

    private UserIdentityEntity createUserIdentity(AuthProvider provider, String providerUserId) {
        return userIdentityRepository.save(UserIdentityEntity.builder()
                .user(user)
                .provider(provider)
                .providerUserId(providerUserId)
                .build());
    }
}
