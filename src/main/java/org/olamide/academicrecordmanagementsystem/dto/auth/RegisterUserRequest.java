package org.olamide.academicrecordmanagementsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Schema(name = "RegisterUserRequest")
public record RegisterUserRequest(
        @NotBlank @Email @Schema(example = "john.doe@miu.edu") String username,
        @NotBlank @Schema(example = "StrongP@ssw0rd!") String password,
        @Schema(example = "[\"REGISTRAR\",\"FACULTY\"]") Set<String> roles
) {}
