package com.hackfintech.entity;

import com.hackfintech.config.CryptoConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Hash determinista del RUT para búsquedas rápidas y validación de unicidad
    @Column(unique = true, nullable = false, length = 64)
    private String rutHash; 

    // RUT encriptado con AES para poder visualizarlo o usarlo cuando sea necesario
    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false)
    private String rutEncriptado;

    private String nombreCompleto;
    
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol = "ROLE_USER"; // Default rol

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DatosSensibles datosSensibles;

    @OneToMany(mappedBy = "usuario")
    private List<AccesoOrganizacion> accesos;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaUltimaModificacion;

    @Version
    private Long version;
}
