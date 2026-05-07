package com.hackfintech.service;

import com.hackfintech.dto.AuditLogDto;
import com.hackfintech.dto.OrgDashboardMetricsDto;
import com.hackfintech.dto.SolicitudArcoDto;
import com.hackfintech.entity.*;
import com.hackfintech.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationDashboardService {

    private final OrganizacionRepository organizacionRepository;
    private final SolicitudConsentimientoRepository solicitudRepository;
    private final AccesoOrganizacionRepository accesoRepository;
    private final SolicitudArcoRepository arcoRepository;
    private final LogAccesoDatosRepository logRepository;

    public OrgDashboardMetricsDto getMetrics(String orgEmail) {
        Organizacion org = getOrg(orgEmail);

        // 1. Churn a 30 días
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long churn = accesoRepository.countByOrganizacionAndEstadoAndFechaUltimaModificacionAfter(
                org, EstadoPermiso.REVOCADO, thirtyDaysAgo
        );

        // 2. Acceptance & Rejection Rate
        long aprobadas = solicitudRepository.countByOrganizacionAndEstado(org, EstadoSolicitud.APROBADA);
        long rechazadas = solicitudRepository.countByOrganizacionAndEstado(org, EstadoSolicitud.RECHAZADA);
        long totalRespondidas = aprobadas + rechazadas;

        double acceptanceRate = 0.0;
        double rejectionRate = 0.0;
        if (totalRespondidas > 0) {
            acceptanceRate = ((double) aprobadas / totalRespondidas) * 100;
            rejectionRate = ((double) rechazadas / totalRespondidas) * 100;
        }

        // 3. Privacy Score
        List<SolicitudConsentimiento> todasLasSolicitudes = solicitudRepository.findByOrganizacion(org);
        double privacyScore = calcularPrivacyScore(todasLasSolicitudes);

        return OrgDashboardMetricsDto.builder()
                .privacyScore(Math.round(privacyScore * 10.0) / 10.0) // Redondear a 1 decimal
                .acceptanceRate(Math.round(acceptanceRate * 10.0) / 10.0)
                .rejectionRate(Math.round(rejectionRate * 10.0) / 10.0)
                .consentChurn30Days(churn)
                .build();
    }

    public List<SolicitudArcoDto> getPendingArcoRequests(String orgEmail) {
        Organizacion org = getOrg(orgEmail);
        
        // Obtener solo pendientes o en proceso
        List<EstadoArco> estadosActivos = Arrays.asList(EstadoArco.PENDIENTE, EstadoArco.EN_PROCESO);
        List<SolicitudArco> solicitudes = arcoRepository.findByOrganizacionAndEstadoIn(org, estadosActivos);

        return solicitudes.stream().map(sol -> {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), sol.getFechaLimiteRespuesta());
            return SolicitudArcoDto.builder()
                    .id(sol.getId().toString())
                    .tipo(sol.getTipo().name())
                    .estado(sol.getEstado().name())
                    .fechaSolicitud(sol.getFechaSolicitud())
                    .fechaLimiteRespuesta(sol.getFechaLimiteRespuesta())
                    .diasRestantes(diasRestantes)
                    .detalleSolicitud(sol.getDetalleSolicitud())
                    .build();
        }).collect(Collectors.toList());
    }

    public List<AuditLogDto> getAuditLogs(String orgEmail) {
        Organizacion org = getOrg(orgEmail);
        List<LogAccesoDatos> logs = logRepository.findByOrganizacionOrderByFechaAccesoDesc(org);

        return logs.stream().map(log -> AuditLogDto.builder()
                .logId(log.getId().toString())
                .tipoDatoConsultado(log.getTipoDatoConsultado())
                .justificacionLegal(log.getJustificacionLegal())
                .fechaAcceso(log.getFechaAcceso())
                .build()
        ).collect(Collectors.toList());
    }

    private Organizacion getOrg(String email) {
        return organizacionRepository.findAll().stream()
                .filter(o -> o.getEmailContacto().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Organización no encontrada"));
    }

    private double calcularPrivacyScore(List<SolicitudConsentimiento> solicitudes) {
        if (solicitudes.isEmpty()) return 100.0; // Puntuación base

        double totalPuntos = 0;
        for (SolicitudConsentimiento s : solicitudes) {
            if ("GREEN".equalsIgnoreCase(s.getAiFlag())) {
                totalPuntos += 100;
            } else if ("YELLOW".equalsIgnoreCase(s.getAiFlag())) {
                totalPuntos += 50;
            } else if ("RED".equalsIgnoreCase(s.getAiFlag())) {
                totalPuntos += 0;
            } else {
                totalPuntos += 100; // Por defecto o sin analizar
            }
        }
        return totalPuntos / solicitudes.size();
    }
}
