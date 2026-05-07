package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConsentResponse {
    private UUID id;
    private String nombreOrganizacion;
    private String industriaOrganizacion;
    private boolean permitirDatosBasicos;
    private boolean permitirDatosSensibles;
    private boolean permitirGeolocalizacion;
    private String estado;
    private LocalDateTime fechaOtorgamiento;
}
