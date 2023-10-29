package com.carspot.app.repository;

import com.carspot.app.entity.ERole;
import com.carspot.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(ERole name);
}
