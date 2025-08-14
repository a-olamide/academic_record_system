package org.olamide.academicrecordmanagementsystem.service.security;

import lombok.RequiredArgsConstructor;
import org.olamide.academicrecordmanagementsystem.model.auth.AppUser;
import org.olamide.academicrecordmanagementsystem.repository.auth.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
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