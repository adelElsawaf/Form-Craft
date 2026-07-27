package com.formcrafter.auth.user;

import com.formcrafter.auth.security.CustomUserDetails;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import com.formcrafter.auth.user_identity.UserIdentityEntity;
import com.formcrafter.auth.user_identity.UserIdentityService;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserIdentityService userIdentityService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        String credentialSecret = userIdentityService
                .findByProviderAndProviderUserId(AuthProvider.EMAIL_AND_PASSWORD, email)
                .map(UserIdentityEntity::getCredentialSecret)
                .orElse(null);

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                credentialSecret
        );
    }
}
