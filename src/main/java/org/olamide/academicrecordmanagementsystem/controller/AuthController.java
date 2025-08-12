package org.olamide.academicrecordmanagementsystem.controller;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.api.ApiResponse;
import org.olamide.academicrecordmanagementsystem.dto.auth.LoginRequest;
import org.olamide.academicrecordmanagementsystem.dto.auth.LoginResponse;
import org.olamide.academicrecordmanagementsystem.service.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserDetailsService uds;
    private final JwtService jwt;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var user = uds.loadUserByUsername(req.username());
//        var token = jwt.generateToken(user, Map.of(
//                "roles", user.getAuthorities().stream().map(a -> a.getAuthority()).toList()
//        ));
        var roles = user.getAuthorities().stream().map(a -> a.getAuthority()).toList();
        var token = jwt.generateToken(user, Map.of("roles", roles));
        var body  = new LoginResponse(token, "Bearer", /* from props if you prefer */ 3_600_000L, roles);
        return ResponseEntity.ok(ApiResponse.ok(body));
    }
}
