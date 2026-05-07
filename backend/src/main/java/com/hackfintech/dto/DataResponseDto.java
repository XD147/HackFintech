package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponseDto {
    private String nombreCompleto;
    private String email;
    
    // Estos campos solo se llenan si el consentimiento lo permite
    private String informacionSalud;
    private String afiliacionPolitica;
    private String orientacionSexual;
}
