package org.olamide.academicrecordmanagementsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "LoginResponse")
public record LoginResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,
        @Schema(example = "Bearer") String tokenType,
        @Schema(example = "3600000") long expiresInMs,
        @Schema(example = "[\"ROLE_ADMIN\",\"ROLE_REGISTRAR\"]") List<String> roles
) {}