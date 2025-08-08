package com.sercurity.services.repositories;

import com.sercurity.services.models.AppRole;
import com.sercurity.services.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}
