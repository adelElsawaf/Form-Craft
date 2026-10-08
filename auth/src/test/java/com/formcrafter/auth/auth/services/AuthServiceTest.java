package com.formcrafter.auth.auth.services;

import com.formcrafter.auth.auth.clients.GoogleOAuthClient;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleTokenDTO;
import com.formcrafter.auth.auth.dtos.google_integrations.GoogleUserDTO;
import com.formcrafter.auth.auth.dtos.requests.GoogleAuthRequest;
import com.formcrafter.auth.auth.dtos.requests.LoginRequest;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import com.formcrafter.auth.auth.dtos.responses.AuthTokensDto;
import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.auth.dtos.responses.LoginResponse;
import com.formcrafter.auth.auth.exceptions.InvalidCredentialsException;
import com.formcrafter.auth.auth.mappers.AuthMapper;
import com.formcrafter.auth.jwt.JwtService;
import com.formcrafter.auth.user.UserService;
import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user.exceptions.UserAlreadyExistsException;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String PASSWORD = "securePass1";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String GOOGLE_CODE = "google-auth-code";
    private static final String GOOGLE_ID_TOKEN = "google-id-token";
    private static final String GOOGLE_ID = "google-user-123";

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private GoogleOAuthClient googleOAuthClient;

    @Mock
    private GoogleTokenVerifierService googleVerifier;

    @InjectMocks
    private AuthService authService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class Register {

        @Test
        void whenValidRequest_createsUserAndLogsIn() {
            RegisterRequest request = sampleRegisterRequest();
            CreateUserRequest createUserRequest = CreateUserRequest.builder().email(EMAIL).build();
            UserDTO user = sampleUserDto();
            AuthUserDTO authUser = sampleAuthUserDto();
            Authentication authentication = mock(Authentication.class);

            when(authMapper.toCreateUserRequest(request)).thenReturn(createUserRequest);
            when(userService.createUser(createUserRequest)).thenReturn(user);
            when(authenticationManager.authenticate(argThat(token ->
                    token instanceof UsernamePasswordAuthenticationToken
                            && EMAIL.equals(token.getPrincipal())
                            && PASSWORD.equals(token.getCredentials())
            ))).thenReturn(authentication);
            when(userService.getUserByEmail(EMAIL)).thenReturn(user);
            when(authMapper.toAuthUserDTO(user)).thenReturn(authUser);
            stubTokenGeneration();

            LoginResponse actual = authService.register(request);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expectedLoginResponse(authUser));
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
            verify(userService).createUser(createUserRequest);
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        void whenCreateUserThrowsUserAlreadyExists_propagates() {
            RegisterRequest request = sampleRegisterRequest();
            CreateUserRequest createUserRequest = CreateUserRequest.builder().email(EMAIL).build();

            when(authMapper.toCreateUserRequest(request)).thenReturn(createUserRequest);
            when(userService.createUser(createUserRequest)).thenThrow(new UserAlreadyExistsException());

            assertThatThrownBy(() -> authService.register(request))
                    .isInstanceOf(UserAlreadyExistsException.class);

            verifyNoInteractions(authenticationManager, jwtService);
            verify(userService, never()).getUserByEmail(any());
        }

        @Test
        void whenLoginFailsAfterCreate_propagatesInvalidCredentials() {
            RegisterRequest request = sampleRegisterRequest();
            CreateUserRequest createUserRequest = CreateUserRequest.builder().email(EMAIL).build();

            when(authMapper.toCreateUserRequest(request)).thenReturn(createUserRequest);
            when(userService.createUser(createUserRequest)).thenReturn(sampleUserDto());
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("bad credentials"));

            assertThatThrownBy(() -> authService.register(request))
                    .isInstanceOf(InvalidCredentialsException.class);

            verify(userService).createUser(createUserRequest);
            verifyNoInteractions(jwtService);
            verify(userService, never()).getUserByEmail(any());
        }
    }

    @Nested
    class Login {

        @Test
        void whenCredentialsValid_returnsTokensAndSetsSecurityContext() {
            LoginRequest request = new LoginRequest(EMAIL, PASSWORD);
            UserDTO user = sampleUserDto();
            AuthUserDTO authUser = sampleAuthUserDto();
            Authentication authentication = mock(Authentication.class);

            when(authenticationManager.authenticate(argThat(token ->
                    token instanceof UsernamePasswordAuthenticationToken
                            && EMAIL.equals(token.getPrincipal())
                            && PASSWORD.equals(token.getCredentials())
            ))).thenReturn(authentication);
            when(userService.getUserByEmail(EMAIL)).thenReturn(user);
            when(authMapper.toAuthUserDTO(user)).thenReturn(authUser);
            stubTokenGeneration();

            LoginResponse actual = authService.login(request);

            assertThat(actual).usingRecursiveComparison().isEqualTo(expectedLoginResponse(authUser));
            assertThat(SecurityContextHolder.getContext().getAuthentication()).isSameAs(authentication);
        }

        @Test
        void whenBadCredentials_throwsInvalidCredentialsException() {
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("bad credentials"));

            assertThatThrownBy(() -> authService.login(new LoginRequest(EMAIL, PASSWORD)))
                    .isInstanceOf(InvalidCredentialsException.class);

            verifyNoInteractions(jwtService);
            verify(userService, never()).getUserByEmail(any());
        }

        @Test
        void whenInternalAuthHasAppExceptionCause_rethrowsAppException() {
            UserNotFoundException cause = new UserNotFoundException(EMAIL);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new InternalAuthenticationServiceException("failed", cause));

            assertThatThrownBy(() -> authService.login(new LoginRequest(EMAIL, PASSWORD)))
                    .isSameAs(cause);

            verifyNoInteractions(jwtService);
        }

        @Test
        void whenInternalAuthHasOtherCause_rethrowsInternalException() {
            InternalAuthenticationServiceException exception =
                    new InternalAuthenticationServiceException("failed", new RuntimeException("boom"));
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(exception);

            assertThatThrownBy(() -> authService.login(new LoginRequest(EMAIL, PASSWORD)))
                    .isSameAs(exception);

            verifyNoInteractions(jwtService);
        }
    }

    @Nested
    class AuthenticateWithGoogle {

        @Test
        void whenExistingGoogleUser_returnsTokensWithoutCreatingUser() throws Exception {
            GoogleUserDTO googleUser = sampleGoogleUser();
            UserDTO user = sampleUserDto();
            AuthUserDTO authUser = sampleAuthUserDto();

            when(googleOAuthClient.exchangeCode(GOOGLE_CODE))
                    .thenReturn(new GoogleTokenDTO("access", GOOGLE_ID_TOKEN));
            when(googleVerifier.verify(GOOGLE_ID_TOKEN)).thenReturn(googleUser);
            when(userService.findByGoogleId(GOOGLE_ID)).thenReturn(Optional.of(user));
            when(authMapper.toAuthUserDTO(user)).thenReturn(authUser);
            stubTokenGeneration();

            LoginResponse actual = authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE));

            assertThat(actual).usingRecursiveComparison().isEqualTo(expectedLoginResponse(authUser));
            verify(userService, never()).createUser(any());
        }

        @Test
        void whenNewGoogleUser_createsUserAndReturnsTokens() throws Exception {
            GoogleUserDTO googleUser = sampleGoogleUser();
            CreateUserRequest createUserRequest = CreateUserRequest.builder().email(EMAIL).build();
            UserDTO createdUser = sampleUserDto();
            AuthUserDTO authUser = sampleAuthUserDto();

            when(googleOAuthClient.exchangeCode(GOOGLE_CODE))
                    .thenReturn(new GoogleTokenDTO("access", GOOGLE_ID_TOKEN));
            when(googleVerifier.verify(GOOGLE_ID_TOKEN)).thenReturn(googleUser);
            when(userService.findByGoogleId(GOOGLE_ID)).thenReturn(Optional.empty());
            when(authMapper.toCreateUserRequest(googleUser)).thenReturn(createUserRequest);
            when(userService.createUser(createUserRequest)).thenReturn(createdUser);
            when(authMapper.toAuthUserDTO(createdUser)).thenReturn(authUser);
            stubTokenGeneration();

            LoginResponse actual = authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE));

            assertThat(actual).usingRecursiveComparison().isEqualTo(expectedLoginResponse(authUser));
            verify(userService).createUser(createUserRequest);
        }

        @Test
        void whenIdTokenMissing_throwsInvalidCredentialsException() {
            when(googleOAuthClient.exchangeCode(GOOGLE_CODE))
                    .thenReturn(new GoogleTokenDTO("access", "  "));

            assertThatThrownBy(() -> authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE)))
                    .isInstanceOf(InvalidCredentialsException.class);

            verifyNoInteractions(googleVerifier, jwtService);
            verify(userService, never()).findByGoogleId(any());
            verify(userService, never()).createUser(any());
        }

        @Test
        void whenExchangeReturnsNull_throwsInvalidCredentialsException() {
            when(googleOAuthClient.exchangeCode(GOOGLE_CODE)).thenReturn(null);

            assertThatThrownBy(() -> authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE)))
                    .isInstanceOf(InvalidCredentialsException.class);

            verifyNoInteractions(googleVerifier, jwtService);
        }

        @Test
        void whenVerifierThrowsGeneralSecurityException_throwsInvalidCredentialsException() throws Exception {
            when(googleOAuthClient.exchangeCode(GOOGLE_CODE))
                    .thenReturn(new GoogleTokenDTO("access", GOOGLE_ID_TOKEN));
            when(googleVerifier.verify(GOOGLE_ID_TOKEN)).thenThrow(new GeneralSecurityException("invalid"));

            assertThatThrownBy(() -> authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE)))
                    .isInstanceOf(InvalidCredentialsException.class);

            verifyNoInteractions(jwtService);
        }

        @Test
        void whenVerifierThrowsIOException_throwsInvalidCredentialsException() throws Exception {
            when(googleOAuthClient.exchangeCode(GOOGLE_CODE))
                    .thenReturn(new GoogleTokenDTO("access", GOOGLE_ID_TOKEN));
            when(googleVerifier.verify(GOOGLE_ID_TOKEN)).thenThrow(new IOException("network"));

            assertThatThrownBy(() -> authService.authenticateWithGoogle(new GoogleAuthRequest(GOOGLE_CODE)))
                    .isInstanceOf(InvalidCredentialsException.class);

            verifyNoInteractions(jwtService);
        }
    }

    private void stubTokenGeneration() {
        when(jwtService.generateAccessToken(EMAIL)).thenReturn(ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(EMAIL)).thenReturn(REFRESH_TOKEN);
    }

    private static LoginResponse expectedLoginResponse(AuthUserDTO authUser) {
        return LoginResponse.builder()
                .user(authUser)
                .tokens(new AuthTokensDto(ACCESS_TOKEN, REFRESH_TOKEN))
                .build();
    }

    private static RegisterRequest sampleRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        return request;
    }

    private static UserDTO sampleUserDto() {
        return UserDTO.builder()
                .id(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }

    private static AuthUserDTO sampleAuthUserDto() {
        return AuthUserDTO.builder()
                .id(USER_ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .build();
    }

    private static GoogleUserDTO sampleGoogleUser() {
        return new GoogleUserDTO(GOOGLE_ID, EMAIL, FIRST_NAME, LAST_NAME);
    }
}
