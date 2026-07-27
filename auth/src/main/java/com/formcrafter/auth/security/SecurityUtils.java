package com.formcrafter.auth.security;

import com.formcrafter.auth.security.exceptions.UnauthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails requireCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
                || !(authentication.getPrincipal() instanceof CustomUserDetails principal)) {
            throw new UnauthenticatedException();
        }

        return principal;
    }
}
