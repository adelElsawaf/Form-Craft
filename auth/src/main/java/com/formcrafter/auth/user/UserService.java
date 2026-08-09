package com.formcrafter.auth.user;

import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user.exceptions.UserAlreadyExistsException;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import com.formcrafter.auth.user_identity.UserIdentityService;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import com.formcrafter.auth.user_identity.exceptions.GoogleAccountAlreadyLinkedException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserIdentityService userIdentityService;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        UserEntity user = userMapper.toEntity(request);

        if (request.getIdentity() != null) {
            user.getIdentities().add(userIdentityService.buildIdentity(user, request.getIdentity()));
        }

        try {
            UserEntity savedUser = userRepository.saveAndFlush(user);
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw mapUniqueConstraintViolation(ex, request);
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> findByGoogleId(String googleId) {
        return userIdentityService
                .findByProviderAndProviderUserId(AuthProvider.GOOGLE, googleId)
                .map(identity -> userMapper.toDto(identity.getUser()));
    }

    private RuntimeException mapUniqueConstraintViolation(
            DataIntegrityViolationException ex,
            CreateUserRequest request
    ) {
        String message = Optional.ofNullable(ex.getMostSpecificCause())
                .map(Throwable::getMessage)
                .orElse("")
                .toLowerCase(Locale.ROOT);

        if (message.contains("(email)")) {
            return new UserAlreadyExistsException();
        }

        if (message.contains("provider_user_id")
                && request.getIdentity() != null
                && request.getIdentity().getProvider() == AuthProvider.GOOGLE) {
            return new GoogleAccountAlreadyLinkedException();
        }

        return ex;
    }
}
