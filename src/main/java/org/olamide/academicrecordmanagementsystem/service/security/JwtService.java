package org.olamide.academicrecordmanagementsystem.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import static io.jsonwebtoken.Jwts.SIG;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTtlMs;

    public JwtService(JwtProperties props) {
        // Prefer Base64 secrets; fallback to raw bytes if not Base64
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(props.secret());
        } catch (IllegalArgumentException notBase64) {
            keyBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);      // ≥32 bytes for HS256
        this.accessTtlMs = props.accessTtlMs();
    }

    /** Generate an access token with custom claims (no deprecated APIs). */
    public String generateToken(UserDetails user, Map<String,Object> customClaims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())                       // set 'sub'
                .claims(customClaims == null ? Map.of() : customClaims) // bulk custom claims
                .issuedAt(Date.from(now))                          // set 'iat'
                .expiration(Date.from(now.plusMillis(accessTtlMs)))// set 'exp'
                .signWith(key, SIG.HS256)                          // sign
                .compact();
    }

    /** Extract username (subject) using the new parser API. */
    public String extractUsername(String token) {
        Claims claims = parse(token).getPayload();
        return claims.getSubject();
    }

    /** Validate common cases: signature, expiration, and subject. */
    public boolean isTokenValid(String token, UserDetails user) {
        try {
            Jws<Claims> jws = parse(token);
            String subject = jws.getPayload().getSubject();
            Date exp = jws.getPayload().getExpiration();
            return StringUtils.hasText(subject)
                    && subject.equals(user.getUsername())
                    && exp != null
                    && exp.after(new Date());
        } catch (Exception e) {
            return false; // signature/format/expiry issues
        }
    }

    /** Centralized parsing with signature verification (new API). */
    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}