package com.formcrafter.auth.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.Instant;

@Data
@Builder
@Schema(name = "ExceptionResponse", description = "Standard error payload returned by the auth service.")
public class ExceptionResponse {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Schema(description = "UTC timestamp when the error occurred", example = "2026-07-20T13:45:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Bad Request")
    private String error;

    @Schema(description = "Human-readable error message", example = "Email is required")
    private String message;

    @Schema(description = "Request path that produced the error", example = "/api/auth/register")
    private String path;

    public static ExceptionResponse of(HttpStatus httpStatus, String message, String path) {
        return ExceptionResponse.builder()
                .timestamp(Instant.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
    }

    public static void write(HttpServletResponse response, HttpStatus httpStatus, String message, String path)
            throws IOException {
        response.resetBuffer();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(MAPPER.writeValueAsString(of(httpStatus, message, path)));
    }
}
