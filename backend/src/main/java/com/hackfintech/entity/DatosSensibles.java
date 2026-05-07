package com.hackfintech.entity;

import com.hackfintech.config.CryptoConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "datos_sensibles")
@Data
public class DatosSensibles {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String biometriaHash; // Hash de datos biométricos
    
    @Convert(converter = CryptoConverter.class)
    @Column(columnDefinition = "TEXT")
    private String informacionSalud;

    @Convert(converter = CryptoConverter.class)
    private String afiliacionPolitica;
    
    @Convert(converter = CryptoConverter.class)
    private String orientacionSexual;
}
