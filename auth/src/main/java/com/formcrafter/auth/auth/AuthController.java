package com.formcrafter.auth.auth;

import com.formcrafter.auth.auth.documentation.apis.GoogleLoginDocumentation;
import com.formcrafter.auth.auth.documentation.apis.LoginDocumentation;
import com.formcrafter.auth.auth.documentation.apis.LogoutDocumentation;
import com.formcrafter.auth.auth.documentation.apis.RegisterDocumentation;
import com.formcrafter.auth.auth.documentation.controllers.AuthControllerDocumentation;
import com.formcrafter.auth.auth.dtos.requests.GoogleAuthRequest;
import com.formcrafter.auth.auth.dtos.requests.LoginRequest;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.auth.dtos.responses.LoginResponse;
import com.formcrafter.auth.auth.services.AuthService;
import com.formcrafter.auth.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AuthControllerDocumentation
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @RegisterDocumentation
    @PostMapping("/register")
    public ResponseEntity<AuthUserDTO> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Register request for email={}", request.getEmail());
        LoginResponse loginResponse = authService.register(request);
        return buildAuthResponse(loginResponse);
    }

    @LoginDocumentation
    @PostMapping("/login")
    public ResponseEntity<AuthUserDTO> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return buildAuthResponse(loginResponse);
    }

    @GoogleLoginDocumentation
    @PostMapping("/google")
    public ResponseEntity<AuthUserDTO> googleLogin(@RequestBody GoogleAuthRequest request) {
        return buildAuthResponse(authService.authenticateWithGoogle(request));
    }

    private ResponseEntity<AuthUserDTO> buildAuthResponse(LoginResponse loginResponse) {
        ResponseCookie accessTokenCookie =
                buildAccessTokenCookie(loginResponse.getTokens().getAccessToken());

        ResponseCookie refreshTokenCookie =
                buildRefreshTokenCookie(loginResponse.getTokens().getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(loginResponse.getUser());
    }

    private ResponseCookie buildAccessTokenCookie(String token) {
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtService.getAccessTokenExpiration())
                .sameSite("Strict")
                .build();
    }

    private ResponseCookie buildRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtService.getRefreshTokenExpiration())
                .sameSite("Strict")
                .build();
    }

    private ResponseCookie expiredCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    @LogoutDocumentation
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredCookie("accessToken").toString())
                .header(HttpHeaders.SET_COOKIE, expiredCookie("refreshToken").toString())
                .build();
    }
}
