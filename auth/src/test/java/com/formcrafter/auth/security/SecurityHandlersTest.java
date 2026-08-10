package com.formcrafter.auth.security;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityHandlersTest {

    @Nested
    class JsonAuthenticationEntryPointTests {

        @Test
        void writesUnauthorizedJsonBody() throws Exception {
            JsonAuthenticationEntryPoint entryPoint = new JsonAuthenticationEntryPoint();
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users/me");
            MockHttpServletResponse response = new MockHttpServletResponse();

            entryPoint.commence(request, response, new BadCredentialsException("unauthenticated"));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            assertThat(response.getContentType()).contains("application/json");
            assertThat(response.getContentAsString())
                    .contains("Authentication is required to access this resource")
                    .contains("/api/users/me")
                    .contains("\"status\":401");
        }
    }

    @Nested
    class JsonAccessDeniedHandlerTests {

        @Test
        void writesForbiddenJsonBody() throws Exception {
            JsonAccessDeniedHandler handler = new JsonAccessDeniedHandler();
            MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin");
            MockHttpServletResponse response = new MockHttpServletResponse();

            handler.handle(request, response, new AccessDeniedException("denied"));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            assertThat(response.getContentType()).contains("application/json");
            assertThat(response.getContentAsString())
                    .contains("You do not have permission to access this resource")
                    .contains("/api/admin")
                    .contains("\"status\":403");
        }
    }
}
