package com.formcrafter.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUserDetailsTest {

    @Test
    void exposesUserDetailsContract() {
        CustomUserDetails details = new CustomUserDetails(1L, "adel@example.com", "secret");

        assertThat(details.getId()).isEqualTo(1L);
        assertThat(details.getUsername()).isEqualTo("adel@example.com");
        assertThat(details.getPassword()).isEqualTo("secret");
        assertThat(details.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isEnabled()).isTrue();
    }
}
