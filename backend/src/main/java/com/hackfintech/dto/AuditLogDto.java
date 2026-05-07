package com.hackfintech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogDto {
    private String logId;
    private String tipoDatoConsultado;
    private String justificacionLegal;
    private LocalDateTime fechaAcceso;
}
