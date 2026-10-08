package com.formcrafter.auth.auth;

import com.formcrafter.auth.auth.dtos.requests.GoogleAuthRequest;
import com.formcrafter.auth.auth.dtos.requests.LoginRequest;
import com.formcrafter.auth.auth.dtos.requests.RegisterRequest;
import com.formcrafter.auth.auth.dtos.responses.AuthTokensDto;
import com.formcrafter.auth.auth.dtos.responses.AuthUserDTO;
import com.formcrafter.auth.auth.dtos.responses.LoginResponse;
import com.formcrafter.auth.auth.exceptions.InvalidCredentialsException;
import com.formcrafter.auth.auth.services.AuthService;
import com.formcrafter.auth.exception.GlobalExceptionHandler;
import com.formcrafter.auth.jwt.JwtService;
import com.formcrafter.auth.security.JsonAccessDeniedHandler;
import com.formcrafter.auth.security.JsonAuthenticationEntryPoint;
import com.formcrafter.auth.security.filters.JwtAuthenticationFilter;
import com.formcrafter.auth.shared.configs.SecurityConfig;
import com.formcrafter.auth.user.exceptions.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        JwtAuthenticationFilter.class,
                        JsonAuthenticationEntryPoint.class,
                        JsonAccessDeniedHandler.class
                }
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";
    private static final String PASSWORD = "securePass1";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String GOOGLE_CODE = "google-auth-code";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Nested
    class Register {

        @Test
        void whenValidRequest_returnsUserAndAuthCookies() throws Exception {
            stubTokenExpirations();
            when(authService.register(any(RegisterRequest.class))).thenReturn(sampleLoginResponse());

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleRegisterRequest())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(USER_ID.intValue()))
                    .andExpect(jsonPath("$.email").value(EMAIL))
                    .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                    .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                    .andExpect(cookie().exists("accessToken"))
                    .andExpect(cookie().value("accessToken", ACCESS_TOKEN))
                    .andExpect(cookie().httpOnly("accessToken", true))
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().value("refreshToken", REFRESH_TOKEN))
                    .andExpect(cookie().httpOnly("refreshToken", true));

            verify(authService).register(any(RegisterRequest.class));
        }

        @Test
        void whenPasswordTooShort_returnsBadRequest() throws Exception {
            RegisterRequest invalid = sampleRegisterRequest();
            invalid.setPassword("short");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("password: Password must be at least 8 characters"))
                    .andExpect(jsonPath("$.path").value("/api/auth/register"));

            verifyNoInteractions(authService);
        }

        @Test
        void whenPasswordBlank_returnsBadRequest() throws Exception {
            RegisterRequest invalid = sampleRegisterRequest();
            invalid.setPassword("   ");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("password: Password is required")))
                    .andExpect(jsonPath("$.path").value("/api/auth/register"));

            verifyNoInteractions(authService);
        }

        @Test
        void whenEmailBlank_returnsBadRequest() throws Exception {
            RegisterRequest invalid = sampleRegisterRequest();
            invalid.setEmail("");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("email: Email is required"))
                    .andExpect(jsonPath("$.path").value("/api/auth/register"));

            verifyNoInteractions(authService);
        }

        @Test
        void whenEmailInvalid_returnsBadRequest() throws Exception {
            RegisterRequest invalid = sampleRegisterRequest();
            invalid.setEmail("not-an-email");

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("email: Invalid email address"))
                    .andExpect(jsonPath("$.path").value("/api/auth/register"));

            verifyNoInteractions(authService);
        }

        @Test
        void whenUserAlreadyExists_returnsConflict() throws Exception {
            when(authService.register(any(RegisterRequest.class)))
                    .thenThrow(new UserAlreadyExistsException());

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleRegisterRequest())))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409))
                    .andExpect(jsonPath("$.message").value("A user with this email already exists"))
                    .andExpect(jsonPath("$.path").value("/api/auth/register"));
        }
    }

    @Nested
    class Login {

        @Test
        void whenValidCredentials_returnsUserAndAuthCookies() throws Exception {
            stubTokenExpirations();
            when(authService.login(any(LoginRequest.class))).thenReturn(sampleLoginResponse());

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequest(EMAIL, PASSWORD))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(EMAIL))
                    .andExpect(cookie().value("accessToken", ACCESS_TOKEN))
                    .andExpect(cookie().value("refreshToken", REFRESH_TOKEN));

            verify(authService).login(any(LoginRequest.class));
        }

        @Test
        void whenInvalidCredentials_returnsUnauthorized() throws Exception {
            when(authService.login(any(LoginRequest.class)))
                    .thenThrow(new InvalidCredentialsException());

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequest(EMAIL, PASSWORD))))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Invalid email or password"))
                    .andExpect(jsonPath("$.path").value("/api/auth/login"));
        }

        @Test
        void whenEmailBlank_returnsBadRequest() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequest("", PASSWORD))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.path").value("/api/auth/login"));

            verifyNoInteractions(authService);
        }

        @Test
        void whenPasswordBlank_returnsBadRequest() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequest(EMAIL, "  "))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.path").value("/api/auth/login"));

            verifyNoInteractions(authService);
        }
    }

    @Nested
    class GoogleLogin {

        @Test
        void whenValidCode_returnsUserAndAuthCookies() throws Exception {
            stubTokenExpirations();
            when(authService.authenticateWithGoogle(any(GoogleAuthRequest.class)))
                    .thenReturn(sampleLoginResponse());

            mockMvc.perform(post("/api/auth/google")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new GoogleAuthRequest(GOOGLE_CODE))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value(EMAIL))
                    .andExpect(cookie().value("accessToken", ACCESS_TOKEN))
                    .andExpect(cookie().value("refreshToken", REFRESH_TOKEN));

            verify(authService).authenticateWithGoogle(any(GoogleAuthRequest.class));
        }

        @Test
        void whenInvalidCredentials_returnsUnauthorized() throws Exception {
            when(authService.authenticateWithGoogle(any(GoogleAuthRequest.class)))
                    .thenThrow(new InvalidCredentialsException());

            mockMvc.perform(post("/api/auth/google")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new GoogleAuthRequest(GOOGLE_CODE))))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Invalid email or password"))
                    .andExpect(jsonPath("$.path").value("/api/auth/google"));
        }
    }

    @Nested
    class Logout {

        @Test
        void clearsAuthCookies() throws Exception {
            mockMvc.perform(post("/api/auth/logout"))
                    .andExpect(status().isOk())
                    .andExpect(cookie().exists("accessToken"))
                    .andExpect(cookie().maxAge("accessToken", 0))
                    .andExpect(cookie().exists("refreshToken"))
                    .andExpect(cookie().maxAge("refreshToken", 0));

            verifyNoInteractions(authService);
        }
    }

    private void stubTokenExpirations() {
        when(jwtService.getAccessTokenExpiration()).thenReturn(Duration.ofMinutes(15));
        when(jwtService.getRefreshTokenExpiration()).thenReturn(Duration.ofDays(7));
    }

    private static RegisterRequest sampleRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        return request;
    }

    private static LoginResponse sampleLoginResponse() {
        return LoginResponse.builder()
                .user(AuthUserDTO.builder()
                        .id(USER_ID)
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .email(EMAIL)
                        .build())
                .tokens(new AuthTokensDto(ACCESS_TOKEN, REFRESH_TOKEN))
                .build();
    }
}
