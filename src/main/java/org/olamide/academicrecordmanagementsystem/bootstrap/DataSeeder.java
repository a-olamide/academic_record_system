package org.olamide.academicrecordmanagementsystem.bootstrap;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.enums.RoleName;
import org.olamide.academicrecordmanagementsystem.model.auth.AppUser;
import org.olamide.academicrecordmanagementsystem.model.auth.Role;
import org.olamide.academicrecordmanagementsystem.repository.auth.AppUserRepository;
import org.olamide.academicrecordmanagementsystem.repository.auth.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final AppUserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        // roles
        for (RoleName n : RoleName.values()) {
            roleRepo.findByName(n).orElseGet(() -> roleRepo.save(Role.builder().name(n).build()));
        }
        // admin user
        if (!userRepo.existsByUsername("admin@miu.edu")) {
            var adminRole = roleRepo.findByName(RoleName.ADMIN).orElseThrow();
            userRepo.save(AppUser.builder()
                    .username("admin@miu.edu")
                    .passwordHash(encoder.encode("TestMe!123"))
                    .roles(Set.of(adminRole))
                    .build());
        }
    }
}
