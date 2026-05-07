package com.hackfintech.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log_accesos_datos")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LogAccesoDatos {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id", nullable = false, updatable = false)
    private Organizacion organizacion;

    @Column(nullable = false, updatable = false)
    private String tipoDatoConsultado; // Ej: "BASICOS", "SENSIBLES", "GEOLOCALIZACION"

    @Column(nullable = false, updatable = false)
    private String justificacionLegal; // Base legal bajo la cual se accedió

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAcceso;
    
    // Este log es append-only (inmutable), no lleva @LastModifiedDate ni @Version.
}
