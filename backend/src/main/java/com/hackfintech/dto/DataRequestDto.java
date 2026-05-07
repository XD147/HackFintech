package com.hackfintech.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DataRequestDto {

    @NotBlank(message = "El RUT del ciudadano es obligatorio")
    private String rutCiudadano;

    @NotBlank(message = "Debe especificar la base o justificación legal")
    private String justificacionLegal;

    private boolean requiereDatosSensibles;
}
