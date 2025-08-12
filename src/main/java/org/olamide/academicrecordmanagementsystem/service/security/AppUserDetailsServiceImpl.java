package org.olamide.academicrecordmanagementsystem.service.security;

import org.olamide.academicrecordmanagementsystem.model.auth.AppUser;
import org.olamide.academicrecordmanagementsystem.repository.auth.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AppUserDetailsServiceImpl implements AppUserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser u = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(u.getUsername())
                .password(u.getPasswordHash())
                .disabled(!u.isEnabled())
                .accountExpired(!u.isAccountNonExpired())
                .accountLocked(!u.isAccountNonLocked())
                .credentialsExpired(!u.isCredentialsNonExpired())
                .authorities(
                        u.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
