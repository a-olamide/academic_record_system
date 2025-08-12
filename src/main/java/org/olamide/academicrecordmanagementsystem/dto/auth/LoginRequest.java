package org.olamide.academicrecordmanagementsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest")
public record LoginRequest(
        @NotBlank @Schema(example = "admin@miu.edu") String username,
        @NotBlank @Schema(example = "ChangeMe!123") String password
) {}
