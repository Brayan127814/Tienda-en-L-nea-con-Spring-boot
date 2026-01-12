package com.TiendaEnLinea.TiendaEnLinea.Repository;

import com.TiendaEnLinea.TiendaEnLinea.Entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByRoleName(String roleName);
}
