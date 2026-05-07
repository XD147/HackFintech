package com.hackfintech.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accesos_organizaciones")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AccesoOrganizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false)
    private Organizacion organizacion;

    private boolean permitirDatosBasicos;
    private boolean permitirDatosSensibles;
    private boolean permitirGeolocalizacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPermiso estado;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaOtorgamiento;

    @LastModifiedDate
    private LocalDateTime fechaUltimaModificacion;

    private LocalDateTime fechaExpiracion;
    
    @Column(length = 64)
    private String receiptHash; // Hash digital inmutable (Recibo)
    
    @Version
    private Long version;
}
