package com.formcrafter.auth.user;

import com.formcrafter.auth.security.CustomUserDetails;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import com.formcrafter.auth.user_identity.UserIdentityEntity;
import com.formcrafter.auth.user_identity.UserIdentityService;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String HASHED_SECRET = "hashed-secret";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserIdentityService userIdentityService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    class LoadUserByUsername {

        @Test
        void whenEmailPasswordIdentityPresent_returnsCustomUserDetailsWithSecret() {
            UserEntity user = sampleUser();
            UserIdentityEntity identity = UserIdentityEntity.builder()
                    .credentialSecret(HASHED_SECRET)
                    .build();

            when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
            when(userIdentityService.findByProviderAndProviderUserId(AuthProvider.EMAIL_AND_PASSWORD, EMAIL))
                    .thenReturn(Optional.of(identity));

            UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

            assertThat(actual).isInstanceOf(CustomUserDetails.class);
            CustomUserDetails details = (CustomUserDetails) actual;
            assertThat(details.getId()).isEqualTo(USER_ID);
            assertThat(details.getUsername()).isEqualTo(EMAIL);
            assertThat(details.getPassword()).isEqualTo(HASHED_SECRET);
            verify(userRepository).findByEmailIgnoreCase(EMAIL);
        }

        @Test
        void whenEmailPasswordIdentityMissing_returnsCustomUserDetailsWithNullPassword() {
            when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(sampleUser()));
            when(userIdentityService.findByProviderAndProviderUserId(AuthProvider.EMAIL_AND_PASSWORD, EMAIL))
                    .thenReturn(Optional.empty());

            UserDetails actual = userDetailsService.loadUserByUsername(EMAIL);

            assertThat(actual.getPassword()).isNull();
            assertThat(actual.getUsername()).isEqualTo(EMAIL);
        }

        @Test
        void whenUserMissing_throwsUserNotFoundException() {
            when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userDetailsService.loadUserByUsername(EMAIL))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(EMAIL);
        }
    }

    private static UserEntity sampleUser() {
        return UserEntity.builder()
                .id(USER_ID)
                .firstName("Adel")
                .lastName("Elsawaf")
                .email(EMAIL)
                .build();
    }
}
