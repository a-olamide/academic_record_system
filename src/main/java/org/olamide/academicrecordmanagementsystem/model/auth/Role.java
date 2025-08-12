package org.olamide.academicrecordmanagementsystem.model.auth;

import jakarta.persistence.*;
import lombok.*;
import org.olamide.academicrecordmanagementsystem.enums.RoleName;

@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(name="uq_role_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private RoleName name;
}