package org.olamide.academicrecordmanagementsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.api.ApiResponse;
import org.olamide.academicrecordmanagementsystem.dto.auth.AssignRolesRequest;
import org.olamide.academicrecordmanagementsystem.dto.auth.RegisterUserRequest;
import org.olamide.academicrecordmanagementsystem.enums.RoleName;
import org.olamide.academicrecordmanagementsystem.exception.NotFoundException;
import org.olamide.academicrecordmanagementsystem.model.auth.AppUser;
import org.olamide.academicrecordmanagementsystem.model.auth.Role;
import org.olamide.academicrecordmanagementsystem.repository.auth.AppUserRepository;
import org.olamide.academicrecordmanagementsystem.repository.auth.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersController {

    private final AppUserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    // Register a new user
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> register(@Valid @RequestBody RegisterUserRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            return ResponseEntity.status(409).body(
                    ApiResponse.of(
                            org.springframework.http.HttpStatus.CONFLICT,
                            org.olamide.academicrecordmanagementsystem.enums.ErrorCode.DATA_INTEGRITY_VIOLATION,
                            "Username already exists: " + req.username(),
                            /* data */ null,
                            "/api/admin/users"
                    )
            );
        }

        var roles = resolveRoles(req.roles());
        var user = AppUser.builder()
                .username(req.username())
                .passwordHash(passwordEncoder.encode(req.password()))
                .roles(roles)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();

        var saved = userRepo.save(user);

        var payload = Map.of(
                "id", saved.getId(),
                "username", saved.getUsername(),
                "roles", saved.getRoles().stream().map(r -> r.getName().name()).toList()
        );
        return ResponseEntity.status(201).body(ApiResponse.created(payload));
    }

    // Assign/replace roles for a user
    @PostMapping("/{username}/roles")
    public ResponseEntity<ApiResponse<Map<String, Object>>> assignRoles(
            @PathVariable String username,
            @Valid @RequestBody AssignRolesRequest req) {

        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        var roles = resolveRoles(req.roles());
        user.setRoles(roles); // replace; change to add-all if you prefer additive behavior
        var saved = userRepo.save(user);

        var payload = Map.of(
                "id", saved.getId(),
                "username", saved.getUsername(),
                "roles", saved.getRoles().stream().map(r -> r.getName().name()).toList()
        );
        return ResponseEntity.ok(ApiResponse.ok(payload));
    }

    // Enable/disable a user
    @PatchMapping("/{username}/enabled")
    public ResponseEntity<ApiResponse<?>> setEnabled(
            @PathVariable String username, @RequestParam boolean value) {
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        user.setEnabled(value);
        var saved = userRepo.save(user);

        var payload = Map.of(
                "id", saved.getId(),
                "username", saved.getUsername(),
                "enabled", saved.isEnabled()
        );
        return ResponseEntity.ok(ApiResponse.ok(payload));
    }

    // List users (lightweight projection)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> list() {
        var list = userRepo.findAll().stream().map(u -> Map.<String, Object>of(
                "id", u.getId(),
                "username", u.getUsername(),
                "enabled", u.isEnabled(),
                "roles", u.getRoles().stream().map(r -> r.getName().name()).toList()
        )).toList();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    // Get one user
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> get(@PathVariable String username) {
        var u = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
        var payload = Map.of(
                "id", u.getId(),
                "username", u.getUsername(),
                "enabled", u.isEnabled(),
                "roles", u.getRoles().stream().map(r -> r.getName().name()).toList()
        );
        return ResponseEntity.ok(ApiResponse.ok(payload));
    }

    // Helper: resolve incoming role strings to Role entities
    private Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of(); // or default role if you wish
        }
        var normalized = roleNames.stream()
                .map(s -> s.trim().toUpperCase(Locale.ROOT))
                .collect(Collectors.toSet());

        Set<Role> roles = new HashSet<>();
        for (String r : normalized) {
            RoleName rn;
            try {
                rn = RoleName.valueOf(r);
            } catch (IllegalArgumentException e) {
                throw new NotFoundException("Unknown role: " + r);
            }
            var role = roleRepo.findByName(rn)
                    .orElseThrow(() -> new NotFoundException("Role not in DB: " + rn));
            roles.add(role);
        }
        return roles;
    }
}
