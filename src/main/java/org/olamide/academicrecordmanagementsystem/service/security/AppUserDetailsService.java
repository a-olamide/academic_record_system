package org.olamide.academicrecordmanagementsystem.service.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface AppUserDetailsService {
    UserDetails loadUserByUsername(String username);
}
