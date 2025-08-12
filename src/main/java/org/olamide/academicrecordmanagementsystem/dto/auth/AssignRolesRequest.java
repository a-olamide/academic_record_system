package org.olamide.academicrecordmanagementsystem.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(name = "AssignRolesRequest")
public record AssignRolesRequest(
        @Schema(example = "john.doe@miu.edu") String username,
        @Schema(example = "[\"FACULTY\"]") Set<String> roles
) {}
