package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudArcoDto {
    private String id;
    private String tipo;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaLimiteRespuesta;
    private Long diasRestantes;
    private String detalleSolicitud;
}
