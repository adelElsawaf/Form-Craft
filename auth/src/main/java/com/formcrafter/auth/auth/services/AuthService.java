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
import com.formcrafter.auth.exception.AppException;
import com.formcrafter.auth.jwt.JwtService;
import com.formcrafter.auth.user.UserService;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final GoogleOAuthClient googleOAuthClient;
    private final GoogleTokenVerifierService googleVerifier;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        userService.createUser(authMapper.toCreateUserRequest(request));
        return login(new LoginRequest(request.getEmail(), request.getPassword()));
    }

    @Transactional
    public LoginResponse authenticateWithGoogle(GoogleAuthRequest request) {
        try {
            GoogleTokenDTO token = googleOAuthClient.exchangeCode(request.code());
            if (token == null || !StringUtils.hasText(token.idToken())) {
                throw new InvalidCredentialsException();
            }

            GoogleUserDTO googleUser = googleVerifier.verify(token.idToken());
            return findOrRegisterGoogleUser(googleUser);
        } catch (GeneralSecurityException | IOException e) {
            throw new InvalidCredentialsException();
        }
    }

    private LoginResponse findOrRegisterGoogleUser(GoogleUserDTO googleUser) {
        return userService.findByGoogleId(googleUser.googleId())
                .map(user -> buildLoginResponse(authMapper.toAuthUserDTO(user)))
                .orElseGet(() -> registerGoogleUser(googleUser));
    }

    private LoginResponse registerGoogleUser(GoogleUserDTO googleUser) {
        UserDTO createdUser = userService.createUser(authMapper.toCreateUserRequest(googleUser));
        return buildLoginResponse(authMapper.toAuthUserDTO(createdUser));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        } catch (InternalAuthenticationServiceException e) {
            if (e.getCause() instanceof AppException appException) {
                throw appException;
            }
            throw e;
        }

        UserDTO user = userService.getUserByEmail(request.getEmail());
        return buildLoginResponse(authMapper.toAuthUserDTO(user));
    }

    private LoginResponse buildLoginResponse(AuthUserDTO user) {
        AuthTokensDto tokens = new AuthTokensDto(
                jwtService.generateAccessToken(user.getEmail()),
                jwtService.generateRefreshToken(user.getEmail())
        );

        return LoginResponse.builder()
                .user(user)
                .tokens(tokens)
                .build();
    }

}
