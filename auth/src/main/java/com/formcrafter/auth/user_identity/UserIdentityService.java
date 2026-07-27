package com.formcrafter.auth.user_identity;

import com.formcrafter.auth.user.UserEntity;
import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserIdentityService {
    private final UserIdentityRepository userIdentityRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByProviderAndProviderUserId(AuthProvider provider, String providerUserId) {
        return userIdentityRepository.existsByProviderAndProviderUserIdIgnoreCase(provider, providerUserId);
    }

    public Optional<UserIdentityEntity> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId) {
        return userIdentityRepository.findByProviderAndProviderUserIdIgnoreCase(provider, providerUserId);
    }

    public UserIdentityEntity buildIdentity(UserEntity user, CreateIdentityRequest request) {
        String credentialSecret = request.getProvider() == AuthProvider.EMAIL_AND_PASSWORD
                && StringUtils.hasText(request.getSecret())
                ? passwordEncoder.encode(request.getSecret())
                : null;

        return UserIdentityEntity.builder()
                .user(user)
                .provider(request.getProvider())
                .providerUserId(request.getProviderUserId())
                .credentialSecret(credentialSecret)
                .build();
    }
}
