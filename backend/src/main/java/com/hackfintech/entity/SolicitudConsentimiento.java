package com.hackfintech.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes_consentimiento")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SolicitudConsentimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    private Organizacion organizacion;

    @Column(nullable = false)
    private String rutCiudadanoHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String proposalJson; // El JSON íntegro propuesto por la organización

    // Resultados del Agente IA
    private String aiFlag; // GREEN, YELLOW, RED
    
    @Column(columnDefinition = "TEXT")
    private String aiAnalysisResumen;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(length = 64)
    private String receiptHash; // Sello de inmutabilidad al aprobar
    
    // PORTABILIDAD
    @Column(length = 50)
    private String requestType = "NORMAL";
    
    private java.util.UUID sourceOrganizationId;
}
