package org.olamide.academicrecordmanagementsystem.repository.auth;

import org.olamide.academicrecordmanagementsystem.model.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.olamide.academicrecordmanagementsystem.enums.RoleName;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
