package org.olamide.academicrecordmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    @Bean AuditorAware<String> auditorAware() { return () -> Optional.of("system"); }

    //add when you add security
//    @Bean
//    public AuditorAware<String> auditorAware() {
//        return () -> {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth == null || !auth.isAuthenticated()) return Optional.empty();
//            return Optional.ofNullable(auth.getName()); // e.g., username/email
//        };
//    }
}
