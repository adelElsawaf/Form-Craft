package com.formcrafter.auth.user;

import com.formcrafter.auth.user.dtos.requests.CreateIdentityRequest;
import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user.exceptions.UserAlreadyExistsException;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import com.formcrafter.auth.user_identity.UserIdentityEntity;
import com.formcrafter.auth.user_identity.UserIdentityService;
import com.formcrafter.auth.user_identity.enums.AuthProvider;
import com.formcrafter.auth.user_identity.exceptions.GoogleAccountAlreadyLinkedException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String GOOGLE_ID = "google-user-123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserIdentityService userIdentityService;

    @InjectMocks
    private UserService userService;

    @Nested
    class ExistsByEmail {

        @Test
        void whenPresent_returnsTrue() {
            when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(true);

            boolean actual = userService.existsByEmail(EMAIL);

            assertThat(actual).isTrue();
            verify(userRepository).existsByEmailIgnoreCase(EMAIL);
        }

        @Test
        void whenNotPresent_returnsFalse() {
            when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(false);

            boolean actual = userService.existsByEmail(EMAIL);

            assertThat(actual).isFalse();
            verify(userRepository).existsByEmailIgnoreCase(EMAIL);
        }
    }

    @Nested
    class GetById {

        @Test
        void whenPresent_returnsUserDTO() {
            UserEntity user = sampleUser();
            UserDTO expected = sampleUserDto();

            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(expected);

            UserDTO actual = userService.getById(USER_ID);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
            verify(userRepository).findById(USER_ID);
            verify(userMapper).toDto(user);
        }

        @Test
        void whenNotPresent_throwsUserNotFoundException() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getById(USER_ID))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(String.valueOf(USER_ID));

            verify(userRepository).findById(USER_ID);
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    class GetUserByEmail {

        @Test
        void whenPresent_returnsUserDTO() {
            UserEntity user = sampleUser();
            UserDTO expected = sampleUserDto();

            when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(expected);

            UserDTO actual = userService.getUserByEmail(EMAIL);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
            verify(userRepository).findByEmailIgnoreCase(EMAIL);
            verify(userMapper).toDto(user);
        }

        @Test
        void whenNotPresent_throwsUserNotFoundException() {
            when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getUserByEmail(EMAIL))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining(EMAIL);

            verify(userRepository).findByEmailIgnoreCase(EMAIL);
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    class FindByGoogleId {

        @Test
        void whenPresent_returnsUserDTO() {
            UserEntity user = sampleUser();
            UserIdentityEntity identity = sampleGoogleIdentity(user);
            UserDTO expected = sampleUserDto();

            when(userIdentityService.findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(Optional.of(identity));
            when(userMapper.toDto(user)).thenReturn(expected);

            Optional<UserDTO> actual = userService.findByGoogleId(GOOGLE_ID);

            assertThat(actual)
                    .isPresent()
                    .get()
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
            verify(userIdentityService).findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);
            verify(userMapper).toDto(user);
            verifyNoInteractions(userRepository);
        }

        @Test
        void whenNotPresent_returnsEmptyOptional() {
            when(userIdentityService.findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID))
                    .thenReturn(Optional.empty());

            Optional<UserDTO> actual = userService.findByGoogleId(GOOGLE_ID);

            assertThat(actual).isEmpty();
            verify(userIdentityService).findByProviderAndProviderUserId(AuthProvider.GOOGLE, GOOGLE_ID);
            verifyNoInteractions(userMapper, userRepository);
        }
    }

    @Nested
    class CreateUser {

        @Test
        void whenIdentityPresent_buildsIdentitySavesAndReturnsUserDTO() {
            CreateUserRequest request = sampleCreateUserRequestWithGoogleIdentity();
            UserEntity mapped = sampleUser();
            UserIdentityEntity identity = sampleGoogleIdentity(mapped);
            UserEntity saved = sampleUser();
            UserDTO expected = sampleUserDto();

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userIdentityService.buildIdentity(mapped, request.getIdentity())).thenReturn(identity);
            when(userRepository.saveAndFlush(mapped)).thenReturn(saved);
            when(userMapper.toDto(saved)).thenReturn(expected);

            UserDTO actual = userService.createUser(request);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
            assertThat(mapped.getIdentities()).containsExactly(identity);
            verify(userMapper).toEntity(request);
            verify(userIdentityService).buildIdentity(mapped, request.getIdentity());
            verify(userRepository).saveAndFlush(mapped);
            verify(userMapper).toDto(saved);
        }

        @Test
        void whenIdentityMissing_savesUserWithoutBuildingIdentity() {
            CreateUserRequest request = sampleCreateUserRequestWithoutIdentity();
            UserEntity mapped = sampleUser();
            UserEntity saved = sampleUser();
            UserDTO expected = sampleUserDto();

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userRepository.saveAndFlush(mapped)).thenReturn(saved);
            when(userMapper.toDto(saved)).thenReturn(expected);

            UserDTO actual = userService.createUser(request);

            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(expected);
            assertThat(mapped.getIdentities()).isEmpty();
            verify(userMapper).toEntity(request);
            verify(userIdentityService, never()).buildIdentity(any(), any());
            verify(userRepository).saveAndFlush(mapped);
            verify(userMapper).toDto(saved);
        }

        @Test
        void whenEmailUniqueConstraintViolated_throwsUserAlreadyExistsException() {
            CreateUserRequest request = sampleCreateUserRequestWithoutIdentity();
            UserEntity mapped = sampleUser();
            DataIntegrityViolationException violation = dataIntegrityViolation(
                    "Key (email)=(" + EMAIL + ") already exists."
            );

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userRepository.saveAndFlush(mapped)).thenThrow(violation);

            assertThatThrownBy(() -> userService.createUser(request))
                    .isInstanceOf(UserAlreadyExistsException.class);

            verifyNoInteractions(userIdentityService);
            verify(userMapper, never()).toDto(any());
        }

        @Test
        void whenGoogleIdentityUniqueConstraintViolated_throwsGoogleAccountAlreadyLinkedException() {
            CreateUserRequest request = sampleCreateUserRequestWithGoogleIdentity();
            UserEntity mapped = sampleUser();
            DataIntegrityViolationException violation = dataIntegrityViolation(
                    "Key (provider, provider_user_id)=(GOOGLE, " + GOOGLE_ID + ") already exists."
            );

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userIdentityService.buildIdentity(mapped, request.getIdentity()))
                    .thenReturn(sampleGoogleIdentity(mapped));
            when(userRepository.saveAndFlush(mapped)).thenThrow(violation);

            assertThatThrownBy(() -> userService.createUser(request))
                    .isInstanceOf(GoogleAccountAlreadyLinkedException.class);

            verify(userMapper, never()).toDto(any());
        }

        @Test
        void whenEmailPasswordIdentityUniqueConstraintViolated_rethrowsDataIntegrityViolationException() {
            CreateUserRequest request = sampleCreateUserRequestWithEmailPasswordIdentity();
            UserEntity mapped = sampleUser();
            DataIntegrityViolationException violation = dataIntegrityViolation(
                    "Key (provider, provider_user_id)=(EMAIL_AND_PASSWORD, " + EMAIL + ") already exists."
            );

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userIdentityService.buildIdentity(mapped, request.getIdentity()))
                    .thenReturn(sampleEmailPasswordIdentity(mapped));
            when(userRepository.saveAndFlush(mapped)).thenThrow(violation);

            assertThatThrownBy(() -> userService.createUser(request))
                    .isSameAs(violation);

            verify(userMapper, never()).toDto(any());
        }

        @Test
        void whenProviderUserIdConstraintViolatedWithoutIdentity_rethrowsDataIntegrityViolationException() {
            CreateUserRequest request = sampleCreateUserRequestWithoutIdentity();
            UserEntity mapped = sampleUser();
            DataIntegrityViolationException violation = dataIntegrityViolation(
                    "Key (provider, provider_user_id)=(GOOGLE, " + GOOGLE_ID + ") already exists."
            );

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userRepository.saveAndFlush(mapped)).thenThrow(violation);

            assertThatThrownBy(() -> userService.createUser(request))
                    .isSameAs(violation);

            verifyNoInteractions(userIdentityService);
            verify(userMapper, never()).toDto(any());
        }

        @Test
        void whenUnknownUniqueConstraintViolated_rethrowsDataIntegrityViolationException() {
            CreateUserRequest request = sampleCreateUserRequestWithGoogleIdentity();
            UserEntity mapped = sampleUser();
            DataIntegrityViolationException violation = dataIntegrityViolation("some unrelated constraint failed");

            when(userMapper.toEntity(request)).thenReturn(mapped);
            when(userIdentityService.buildIdentity(mapped, request.getIdentity()))
                    .thenReturn(sampleGoogleIdentity(mapped));
            when(userRepository.saveAndFlush(mapped)).thenThrow(violation);

            assertThatThrownBy(() -> userService.createUser(request))
                    .isSameAs(violation);

            verify(userMapper, never()).toDto(any());
        }
    }

    private static UserEntity sampleUser() {
        return UserEntity.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
    }

    private static UserDTO sampleUserDto() {
        return UserDTO.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
    }

    private static UserIdentityEntity sampleGoogleIdentity(UserEntity user) {
        return UserIdentityEntity.builder()
                .user(user)
                .provider(AuthProvider.GOOGLE)
                .providerUserId(GOOGLE_ID)
                .build();
    }

    private static UserIdentityEntity sampleEmailPasswordIdentity(UserEntity user) {
        return UserIdentityEntity.builder()
                .user(user)
                .provider(AuthProvider.EMAIL_AND_PASSWORD)
                .providerUserId(EMAIL)
                .build();
    }

    private static CreateUserRequest sampleCreateUserRequestWithGoogleIdentity() {
        return CreateUserRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .identity(CreateIdentityRequest.builder()
                        .provider(AuthProvider.GOOGLE)
                        .providerUserId(GOOGLE_ID)
                        .build())
                .build();
    }

    private static CreateUserRequest sampleCreateUserRequestWithEmailPasswordIdentity() {
        return CreateUserRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .identity(CreateIdentityRequest.builder()
                        .provider(AuthProvider.EMAIL_AND_PASSWORD)
                        .providerUserId(EMAIL)
                        .secret("plain-secret")
                        .build())
                .build();
    }

    private static CreateUserRequest sampleCreateUserRequestWithoutIdentity() {
        return CreateUserRequest.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .identity(null)
                .build();
    }

    private static DataIntegrityViolationException dataIntegrityViolation(String causeMessage) {
        return new DataIntegrityViolationException("duplicate", new RuntimeException(causeMessage));
    }
}
