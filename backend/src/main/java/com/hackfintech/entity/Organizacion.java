package com.hackfintech.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "organizaciones")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Organizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String rut; // Identificador legal de la organización

    @Column(nullable = false)
    private String nombre;

    private String industria; // Ej: Salud, Banca, Retail

    @Column(nullable = false)
    private String emailContacto;
    
    private String webhookUrlRevocacion;

    @OneToMany(mappedBy = "organizacion")
    private List<AccesoOrganizacion> accesosConcedidos;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime fechaUltimaModificacion;

    @Version
    private Long version;
}
