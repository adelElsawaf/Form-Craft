package com.formcrafter.auth.user;

import com.formcrafter.auth.shared.config.PostgresTestcontainersConfig;
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
class UserRepositoryTest {

    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String EMAIL_DIFFERENT_CASE = "AdEL_Elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";

    @Autowired
    private UserRepository userRepository;

    @Nested
    class FindByEmailIgnoreCase {

        @Test
        void whenExactMatch_returnsUser() {
            UserEntity expected = createUser(EMAIL);

            Optional<UserEntity> actual = userRepository.findByEmailIgnoreCase(EMAIL);

            assertThat(actual)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenCaseIsDifferent_returnsUser() {
            UserEntity expected = createUser(EMAIL);

            Optional<UserEntity> actual = userRepository.findByEmailIgnoreCase(EMAIL_DIFFERENT_CASE);

            assertThat(actual)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
        }

        @Test
        void whenNoMatch_returnsEmptyOptional() {
            Optional<UserEntity> actual = userRepository.findByEmailIgnoreCase(EMAIL);

            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class ExistsByEmailIgnoreCase {

        @Test
        void whenExactMatch_returnsTrue() {
            createUser(EMAIL);

            boolean actual = userRepository.existsByEmailIgnoreCase(EMAIL);

            assertThat(actual).isTrue();
        }

        @Test
        void whenCaseIsDifferent_returnsTrue() {
            createUser(EMAIL);

            boolean actual = userRepository.existsByEmailIgnoreCase(EMAIL_DIFFERENT_CASE);

            assertThat(actual).isTrue();
        }

        @Test
        void whenNoMatch_returnsFalse() {
            boolean actual = userRepository.existsByEmailIgnoreCase(EMAIL);

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
}
