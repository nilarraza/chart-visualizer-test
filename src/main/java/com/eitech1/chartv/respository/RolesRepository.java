package com.eitech1.chartv.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eitech1.chartv.Entity.MRoles;
import com.eitech1.chartv.Entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Optional<Roles> findByName(MRoles name);
}
