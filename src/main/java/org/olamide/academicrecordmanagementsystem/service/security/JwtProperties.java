package org.olamide.academicrecordmanagementsystem.service.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        String secret,
        long accessTtlMs
) {
}
