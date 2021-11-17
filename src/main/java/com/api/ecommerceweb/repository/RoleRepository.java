package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.enumm.ERole;
import com.api.ecommerceweb.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getByName(ERole role);

}
