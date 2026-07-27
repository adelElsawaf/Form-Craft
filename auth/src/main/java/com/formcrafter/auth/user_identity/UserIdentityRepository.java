package com.formcrafter.auth.user_identity;

import com.formcrafter.auth.user_identity.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentityEntity, Long> {
    Optional<UserIdentityEntity> findByProviderAndProviderUserIdIgnoreCase(AuthProvider provider, String providerUserId);
    boolean existsByProviderAndProviderUserIdIgnoreCase(AuthProvider provider, String providerUserId);
}
