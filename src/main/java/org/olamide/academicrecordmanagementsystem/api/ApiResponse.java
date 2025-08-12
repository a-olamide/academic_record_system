package org.olamide.academicrecordmanagementsystem.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.olamide.academicrecordmanagementsystem.enums.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiResponse", description = "Standard API envelope for Student Enrollment Management System")
public record ApiResponse<T>(
        @Schema(example = "200") int httpStatus,          // e.g., 200, 201, 404
        @Schema(example = "OK") String httpStatusText,   // e.g., "OK", "CREATED", "NOT_FOUND"
        T data,                  // payload for success; null for errors
        @Schema(implementation = ErrorCode.class) ErrorCode errorCode,     // enum; null for success
        String errorMessage,     // human-friendly; null for success
        @Schema(example = "/api/v1/students") String path,             // request path for diagnostics
        @Schema(example = "2025-08-11T14:22:00Z") OffsetDateTime timestamp
) {
    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(
                status.value(),
                status.name(),
                data,
                null,
                null,
                null,
                OffsetDateTime.now()
        );
    }
    public static <T> ApiResponse<T> ok(T data) {
        return success(HttpStatus.OK, data);
    }
    public static <T> ApiResponse<T> created(T data) {
        return success(HttpStatus.CREATED, data);
    }
    public static <T> ApiResponse<T> badRequest() {
        return success(HttpStatus.BAD_REQUEST, null);
    }
    public static <T> ApiResponse<T> unauthorized() {
        return success(HttpStatus.UNAUTHORIZED, null);
    }
    public static <T> ApiResponse<T> of(HttpStatus status, ErrorCode errorCode, String errorMessage, T data, String path) {
        return new ApiResponse<>(
                status.value(),
                status.name(),
                data,
                errorCode,
                errorMessage,
                path,
                OffsetDateTime.now()
        );
    }
    public static ApiResponse<Void> error(HttpStatus status, ErrorCode errorCode, String errorMessage, String path) {
        return new ApiResponse<>(
                status.value(),
                status.name(),
                null,
                errorCode,
                errorMessage,
                path,
                OffsetDateTime.now()
        );
    }


}

