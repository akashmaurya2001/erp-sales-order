package com.precisioncast.erp.auth.repository;

import com.precisioncast.erp.auth.entity.Role;
import com.precisioncast.erp.auth.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleType roleName);
}
