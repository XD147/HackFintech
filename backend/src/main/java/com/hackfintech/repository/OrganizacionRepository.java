package com.hackfintech.repository;

import com.hackfintech.entity.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizacionRepository extends JpaRepository<Organizacion, UUID> {
    Optional<Organizacion> findByRut(String rut);
}
