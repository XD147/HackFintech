package com.hackfintech.repository;

import com.hackfintech.entity.EstadoSolicitud;
import com.hackfintech.entity.SolicitudConsentimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudConsentimientoRepository extends JpaRepository<SolicitudConsentimiento, UUID> {
    List<SolicitudConsentimiento> findByRutCiudadanoHashAndEstado(String rutCiudadanoHash, EstadoSolicitud estado);
    List<SolicitudConsentimiento> findByOrganizacion(com.hackfintech.entity.Organizacion organizacion);
    long countByOrganizacionAndEstado(com.hackfintech.entity.Organizacion organizacion, EstadoSolicitud estado);
}
