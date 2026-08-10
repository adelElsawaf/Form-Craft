package com.formcrafter.auth.exception;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionResponseTest {

    @Nested
    class Of {

        @Test
        void buildsResponseFromStatusMessageAndPath() {
            ExceptionResponse actual = ExceptionResponse.of(
                    HttpStatus.BAD_REQUEST,
                    "Email is required",
                    "/api/auth/register"
            );

            assertThat(actual.getStatus()).isEqualTo(400);
            assertThat(actual.getError()).isEqualTo("Bad Request");
            assertThat(actual.getMessage()).isEqualTo("Email is required");
            assertThat(actual.getPath()).isEqualTo("/api/auth/register");
            assertThat(actual.getTimestamp()).isNotNull();
        }
    }

    @Nested
    class Write {

        @Test
        void writesJsonBodyOntoServletResponse() throws Exception {
            MockHttpServletResponse response = new MockHttpServletResponse();

            ExceptionResponse.write(
                    response,
                    HttpStatus.UNAUTHORIZED,
                    "Authentication is required to access this resource",
                    "/api/users/me"
            );

            assertThat(response.getStatus()).isEqualTo(401);
            assertThat(response.getContentType()).contains("application/json");
            assertThat(response.getContentAsString())
                    .contains("\"status\":401")
                    .contains("Authentication is required to access this resource")
                    .contains("/api/users/me");
        }
    }
}
