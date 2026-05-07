package com.hackfintech.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes_arco")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SolicitudArco {

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
    private TipoArco tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoArco estado = EstadoArco.PENDIENTE;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(nullable = false)
    private LocalDateTime fechaLimiteRespuesta; // 15 días hábiles a partir de fechaSolicitud

    private String detalleSolicitud;

    private String respuestaOrganizacion;
    
    private LocalDateTime fechaResolucion;
}
