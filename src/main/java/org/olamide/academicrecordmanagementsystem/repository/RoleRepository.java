package org.olamide.academicrecordmanagementsystem.repository;

import org.olamide.academicrecordmanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.olamide.academicrecordmanagementsystem.enums.RoleName;


import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
