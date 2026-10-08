package com.formcrafter.auth.user;

import com.formcrafter.auth.exception.GlobalExceptionHandler;
import com.formcrafter.auth.security.CustomUserDetails;
import com.formcrafter.auth.security.JsonAccessDeniedHandler;
import com.formcrafter.auth.security.JsonAuthenticationEntryPoint;
import com.formcrafter.auth.security.filters.JwtAuthenticationFilter;
import com.formcrafter.auth.shared.configs.SecurityConfig;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import com.formcrafter.auth.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
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
class UserControllerTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class GetLoggedInUser {

        @Test
        void whenAuthenticated_returnsCurrentUser() throws Exception {
            authenticateAsCurrentUser();
            UserDTO expected = sampleUserDto();

            when(userService.getById(USER_ID)).thenReturn(expected);

            mockMvc.perform(get("/api/users/me").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(USER_ID.intValue()))
                    .andExpect(jsonPath("$.email").value(EMAIL))
                    .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                    .andExpect(jsonPath("$.lastName").value(LAST_NAME));

            verify(userService).getById(USER_ID);
        }

        @Test
        void whenUnauthenticated_returnsUnauthorized() throws Exception {
            mockMvc.perform(get("/api/users/me").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value("Authentication is required to access this resource"))
                    .andExpect(jsonPath("$.path").value("/api/users/me"));

            verifyNoInteractions(userService);
        }

        @Test
        void whenUserNotFound_returnsNotFound() throws Exception {
            authenticateAsCurrentUser();

            when(userService.getById(USER_ID)).thenThrow(new UserNotFoundException(USER_ID));

            mockMvc.perform(get("/api/users/me").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("User not found with id: " + USER_ID))
                    .andExpect(jsonPath("$.path").value("/api/users/me"));

            verify(userService).getById(USER_ID);
        }
    }

    private static void authenticateAsCurrentUser() {
        CustomUserDetails principal = new CustomUserDetails(USER_ID, EMAIL, "secret");
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static UserDTO sampleUserDto() {
        return UserDTO.builder()
                .id(USER_ID)
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .build();
    }
}
