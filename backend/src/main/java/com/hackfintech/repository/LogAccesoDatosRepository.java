package com.hackfintech.repository;

import com.hackfintech.entity.LogAccesoDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogAccesoDatosRepository extends JpaRepository<LogAccesoDatos, UUID> {
    java.util.List<LogAccesoDatos> findByOrganizacionOrderByFechaAccesoDesc(com.hackfintech.entity.Organizacion organizacion);
}
