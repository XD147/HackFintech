package com.hackfintech.repository;

import com.hackfintech.entity.AccesoOrganizacion;
import com.hackfintech.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccesoOrganizacionRepository extends JpaRepository<AccesoOrganizacion, UUID> {
    List<AccesoOrganizacion> findByUsuario(Usuario usuario);
    long countByOrganizacionAndEstadoAndFechaUltimaModificacionAfter(com.hackfintech.entity.Organizacion organizacion, com.hackfintech.entity.EstadoPermiso estado, java.time.LocalDateTime date);
}
