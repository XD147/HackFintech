package com.hackfintech.repository;

import com.hackfintech.entity.EstadoArco;
import com.hackfintech.entity.Organizacion;
import com.hackfintech.entity.SolicitudArco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitudArcoRepository extends JpaRepository<SolicitudArco, UUID> {
    List<SolicitudArco> findByOrganizacionAndEstadoIn(Organizacion organizacion, List<EstadoArco> estados);
    List<SolicitudArco> findByRutCiudadanoHash(String rutCiudadanoHash);
}
