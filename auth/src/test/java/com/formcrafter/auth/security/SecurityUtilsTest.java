package com.formcrafter.auth.security;

import com.formcrafter.auth.security.exceptions.UnauthenticatedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SecurityUtilsTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class RequireCurrentUser {

        @Test
        void whenCustomUserDetailsAuthenticated_returnsPrincipal() {
            CustomUserDetails principal = new CustomUserDetails(USER_ID, EMAIL, "secret");
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities())
            );

            assertThat(SecurityUtils.requireCurrentUser()).isSameAs(principal);
        }

        @Test
        void whenAuthenticationMissing_throwsUnauthenticatedException() {
            assertThatThrownBy(SecurityUtils::requireCurrentUser)
                    .isInstanceOf(UnauthenticatedException.class);
        }

        @Test
        void whenAnonymous_throwsUnauthenticatedException() {
            SecurityContextHolder.getContext().setAuthentication(
                    new AnonymousAuthenticationToken(
                            "key",
                            "anonymousUser",
                            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                    )
            );

            assertThatThrownBy(SecurityUtils::requireCurrentUser)
                    .isInstanceOf(UnauthenticatedException.class);
        }

        @Test
        void whenAuthenticationNotAuthenticated_throwsUnauthenticatedException() {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(EMAIL, "password")
            );

            assertThatThrownBy(SecurityUtils::requireCurrentUser)
                    .isInstanceOf(UnauthenticatedException.class);
        }

        @Test
        void whenPrincipalIsNotCustomUserDetails_throwsUnauthenticatedException() {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            EMAIL,
                            null,
                            AuthorityUtils.createAuthorityList("ROLE_USER")
                    )
            );

            assertThatThrownBy(SecurityUtils::requireCurrentUser)
                    .isInstanceOf(UnauthenticatedException.class);
        }
    }
}
