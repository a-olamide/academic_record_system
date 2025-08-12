package org.olamide.academicrecordmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users",
        indexes = @Index(name="ix_users_username", columnList="username", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=128)
    private String username; // email is a good username

    @Column(nullable=false, length=255)
    private String passwordHash;

    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
